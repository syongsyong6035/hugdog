package spring.hugme.domain.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.hugme.domain.auth.dto.request.SignUpRequest.SignUp;
import spring.hugme.domain.auth.dto.response.LoginResponse;
import spring.hugme.domain.user.entity.Member;
import spring.hugme.domain.user.repository.UserRepository;
import spring.hugme.global.error.exceptions.AuthApiException;
import spring.hugme.global.response.ResponseCode;
import spring.hugme.infra.jwt.JwtProvider;
import spring.hugme.infra.redis.RedisService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    public Map<String, String> signup(SignUp dto) {
        if (userRepository.existsByUserId(dto.getUserId()))
            throw new AuthApiException(ResponseCode.CONFLICT_EXIST_USER);
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new AuthApiException(ResponseCode.CONFLICT_EXIST_EMAIL);

        Member user = Member.builder()
            .id(dto.getId())
            .userId(dto.getUserId())
            .password(passwordEncoder.encode(dto.getPassword()))
            .email(dto.getEmail())
            .name(dto.getName())
            .birthday(dto.getBirthday())
            .phone(dto.getPhone())
            .build();

        userRepository.save(user);
        return Map.of("userId", dto.getUserId());
    }

    public LoginResponse login(String userId, String password, HttpServletResponse response) {
        Member storedUser = userRepository.findByUserId(userId)
            .orElseThrow(() -> new AuthApiException(ResponseCode.NOT_FOUND_USER));

        if (!storedUser.checkPassword(passwordEncoder, password))
            throw new AuthApiException(ResponseCode.BAD_CREDENTIAL);

        String accessToken = jwtProvider.generateAccessToken(storedUser.getUserId());
        String refreshToken = jwtProvider.generateRefreshToken(storedUser.getUserId());

        redisService.saveRefreshToken(storedUser.getUserId(), refreshToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(7 * 24 * 60 * 60)
            .sameSite("None")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new LoginResponse(accessToken, userId);

    }

    @Transactional
    public LoginResponse reissueAccessToken(String refreshToken) {
        String userId = jwtProvider.validateRefreshToken(refreshToken);
        String storedRefreshToken = redisService.getRefreshToken(userId);
        if (storedRefreshToken == null) {
            throw new AuthApiException(ResponseCode.REFRESH_TOKEN_EXPIRED);
        }

        if (!refreshToken.equals(storedRefreshToken)) {
            throw new AuthApiException(ResponseCode.MISMATCH_TOKEN);
        }
        String newAccessToken = jwtProvider.generateAccessToken(userId);
        return new LoginResponse(newAccessToken, userId);
    }

    /**
     * 로그아웃
     *
     * @param userId
     */
    public void logout(String userId) {
        String authenticatedUserId = (String) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();

        log.info("로그아웃 요청 - RequestUserId: {}, TokenUserId: {}", userId, authenticatedUserId);

        if (!userId.equals(authenticatedUserId)) {
            log.warn("토큰 주체와 요청된 사용자 불일치");
            throw new AuthApiException(ResponseCode.MISMATCH_TOKEN);
        }

        redisService.deleteRefreshToken(userId);
        log.info("로그아웃 완료: {}", userId);
    }
}

