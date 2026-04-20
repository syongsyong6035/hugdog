package spring.hugme.global.error.exceptions;

import spring.hugme.global.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthApiException extends CommonException {

    // ResponseCode만 있는 경우
    public AuthApiException(ResponseCode code) {
        super(code);
    }

    // ResponseCode + 원인 예외
    public AuthApiException(ResponseCode code, Exception e) {
        super(code, e);
        log.error(e.getMessage(), e);
    }
}
