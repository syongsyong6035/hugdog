package spring.hugme.domain.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.hugme.domain.auth.dto.request.LoginRequest;
import spring.hugme.domain.auth.dto.request.SignUpRequest;
import spring.hugme.domain.auth.dto.response.LoginResponse;
import spring.hugme.domain.auth.service.AuthService;
import spring.hugme.global.controller.BaseController;
import spring.hugme.global.response.CommonApiResponse;
import spring.hugme.global.response.ResponseCode;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(BaseController.API_V1 + "/auth")
public class AuthController extends BaseController {

    private final AuthService authService;

    /**
     * 로그인 - Access Token: Response Body로 반환 - Refresh Token: HttpOnly 쿠키로 설정
     */
    @PostMapping("/login")
    public CommonApiResponse<LoginResponse> login(
        @RequestBody @Valid LoginRequest request,
        HttpServletResponse response
    ) {
        log.info("로그인 요청: userId={}", request.getUserId());
        LoginResponse loginResponse = authService.login(
            request.getUserId(),
            request.getPassword(),
            response
        );
        return CommonApiResponse.success(
            ResponseCode.OK,
            "정상적으로 로그인이 완료되었습니다.",
            loginResponse
        );
    }

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public CommonApiResponse<Map<String, String>> signup(
        @RequestBody @Valid SignUpRequest.SignUp dto
    ) {
        log.info("회원가입 요청: userId={}", dto.getUserId());
        Map<String, String> data = authService.signup(dto);
        return CommonApiResponse.success(
            ResponseCode.CREATED,
            "정상적으로 회원가입이 완료되었습니다.",
            data
        );
    }

    /**
     * Access Token 재발급 - Refresh Token을 쿠키에서 가져옴 - userId는 Refresh Token에서 추출
     */
    @PostMapping("/reissue")
    public CommonApiResponse<LoginResponse> reissue(
        @CookieValue("refreshToken") String refreshToken
    ) {
        log.info("Access Token 재발급 요청");
        LoginResponse response = authService.reissueAccessToken(refreshToken);
        return CommonApiResponse.success(
            ResponseCode.OK,
            "정상적으로 Access Token이 재발급되었습니다.",
            response
        );
    }

    /**
     * 로그아웃 - Redis에서 Refresh Token 삭제 - Refresh Token 쿠키 삭제
     */
    @PostMapping("/logout")
    public CommonApiResponse<Void> logout(
        @AuthenticationPrincipal String userId,
        @CookieValue(value = "refreshToken", required = false) String refreshToken,
        HttpServletResponse response
    ) {
        log.info("로그아웃 요청: userId={}", userId);
        authService.logout(userId);

        // Refresh Token 쿠키 삭제
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        return CommonApiResponse.success(
            ResponseCode.NO_CONTENT,
            "정상적으로 로그아웃이 완료되었습니다."
        );
    }


}