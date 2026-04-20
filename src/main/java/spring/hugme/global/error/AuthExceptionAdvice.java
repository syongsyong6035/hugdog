package spring.hugme.global.error;

import spring.hugme.global.error.exceptions.AuthApiException;
import spring.hugme.global.response.CommonApiResponse;
import spring.hugme.global.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class AuthExceptionAdvice {

    // AuthApiException 처리
    @ExceptionHandler(AuthApiException.class)
    @ResponseBody
    public ResponseEntity<CommonApiResponse<String>> handleAuthException(AuthApiException ex) {
        log.error("AuthApiException 발생: code={}, message={}", ex.code().code(), ex.getMessage());
        String message = ex.getMessage() != null ? ex.getMessage() : ex.code().message();
        return ResponseEntity
                .status(ex.code().status())
                .body(CommonApiResponse.error(ex.code(), message, null));
    }

    // Spring Security AuthenticationException 처리
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseEntity<CommonApiResponse<String>> handleAuthenticationException(AuthenticationException ex) {
        log.error("AuthenticationException 발생: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(CommonApiResponse.error(
                        ResponseCode.UNAUTHORIZED,
                        "로그인 실패: 인증 정보가 올바르지 않습니다.",
                        null
                ));
    }

    // Validation 에러 처리 (회원가입 등에서 @Valid 사용 시)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<CommonApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException ex) {
        log.error("Validation 에러 발생: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonApiResponse.error(
                        ResponseCode.BAD_REQUEST,
                        "입력값 검증 실패",
                        errors
                ));
    }

    // IllegalArgumentException 처리
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<CommonApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("IllegalArgumentException 발생: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonApiResponse.error(
                        ResponseCode.BAD_REQUEST,
                        ex.getMessage(),
                        null
                ));
    }

    // 기타 모든 예외 처리
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<CommonApiResponse<String>> handleException(Exception ex) {
        log.error("예상치 못한 에러 발생", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonApiResponse.error(
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "서버 내부 오류가 발생했습니다.",
                        null
                ));
    }
}