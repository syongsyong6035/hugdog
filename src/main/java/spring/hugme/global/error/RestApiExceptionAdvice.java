package spring.hugme.global.error;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.hugme.global.error.exceptions.CommonException;
import spring.hugme.global.response.CommonApiResponse;
import spring.hugme.global.response.ResponseCode;

@RestControllerAdvice(basePackages = "spring.hugme.app.controller")
@Slf4j
public class RestApiExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonApiResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("[Validation Error] {}", ex.getMessage(), ex);

        Map<String, String> errors = new LinkedHashMap<>();
        ex.getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));

        return ResponseEntity
                .badRequest()
                .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST, "입력값이 올바르지 않습니다.", errors));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CommonApiResponse<String>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.error("[Method Not Supported] {}", ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST, "허용되지 않은 요청 방식입니다.", ex.getMessage()));
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<CommonApiResponse<String>> handleCommonException(CommonException ex) {
        log.error("[CommonException] code: {}, message: {}", ex.code().code(), ex.code().message(), ex);

        return ResponseEntity
                .status(ex.code().status())
                .body(CommonApiResponse.error(ex.code()));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<CommonApiResponse<String>> handleAuthorizationDenied(AuthorizationDeniedException ex) {
        log.error("[Authorization Denied] {}", ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(CommonApiResponse.error(ResponseCode.UNAUTHORIZED, "인증이 필요한 요청입니다.", null));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonApiResponse<String>> handleRuntimeException(RuntimeException ex) {
        log.error("[RuntimeException] {}", ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.", null));
    }
}
