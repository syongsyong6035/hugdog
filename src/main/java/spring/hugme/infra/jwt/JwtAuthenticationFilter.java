package spring.hugme.infra.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.hugme.global.error.exceptions.AuthApiException;
import spring.hugme.global.response.CommonApiResponse;
import spring.hugme.global.response.ResponseCode;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        log.info("Authorization Header: {}", header != null ? "있음" : "없음");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = header.substring(7);

        try {
            String userId = jwtProvider.validateAccessToken(accessToken);

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    userId, null, List.of(new SimpleGrantedAuthority("USER"))
                );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (AuthApiException ex) {
            log.error("토큰 검증 실패: {}", ex.getMessage());
            handleAuthException(response, ex);
        } catch (Exception ex) {
            log.error("토큰 처리 중 예외 발생", ex);
            sendErrorResponse(response, ResponseCode.INVALID_TOKEN, "토큰 처리 중 오류가 발생했습니다.");
        }
    }

    private void handleAuthException(HttpServletResponse response, AuthApiException ex)
        throws IOException {
        switch (ex.getCode()) {
            case REFRESH_TOKEN_EXPIRED ->
                sendErrorResponse(response, ResponseCode.REFRESH_TOKEN_EXPIRED,
                    "리프레시 토큰이 만료되었습니다. 다시 로그인해주세요.");
            case TOKEN_EXPIRED -> sendErrorResponse(response, ResponseCode.TOKEN_EXPIRED,
                "액세스 토큰이 만료되었습니다. 토큰을 재발급해주세요.");
            case INVALID_ACCESS_TOKEN ->
                sendErrorResponse(response, ResponseCode.INVALID_ACCESS_TOKEN,
                    "유효하지 않은 토큰입니다.");
            case INVALID_TOKEN -> sendErrorResponse(response, ResponseCode.INVALID_TOKEN,
                "인증에 실패했습니다.");
            case MISMATCH_TOKEN -> sendErrorResponse(response, ResponseCode.MISMATCH_TOKEN,
                "토큰 정보가 일치하지 않습니다.");
            case FAILED_TOKEN_PARSE -> sendErrorResponse(response, ResponseCode.FAILED_TOKEN_PARSE,
                "토큰 파싱에 실패했습니다.");
            case NOT_EXIST_PRE_AUTH_CREDENTIAL ->
                sendErrorResponse(response, ResponseCode.NOT_EXIST_PRE_AUTH_CREDENTIAL,
                    "인증 정보가 존재하지 않습니다.");
            default -> sendErrorResponse(response, ResponseCode.INVALID_TOKEN,
                "알 수 없는 토큰 오류입니다.");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, ResponseCode code, String message)
        throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        CommonApiResponse<?> errorResponse = CommonApiResponse.error(code, message);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}