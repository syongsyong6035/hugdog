package spring.hugme.global.response;

import org.springframework.http.HttpStatus;

public enum ResponseCode {
    // Success
    OK("200", HttpStatus.OK, "OK"),
    CONTINUE("1000", HttpStatus.CONTINUE, "Continue"),
    CREATED("201", HttpStatus.CREATED, "Created"),
    ACCEPTED("202", HttpStatus.ACCEPTED, "Accepted"),
    NO_CONTENT("204", HttpStatus.NO_CONTENT, "No Content"),
    // Client Error
    BAD_REQUEST("400", HttpStatus.BAD_REQUEST, "Bad Request."),
    UNAUTHORIZED("401", HttpStatus.UNAUTHORIZED, "Authentication required"),
    BAD_CREDENTIAL("4011", HttpStatus.UNAUTHORIZED, "Wrong credentials."),
    INVALID_CODE("4012", HttpStatus.UNAUTHORIZED, "Invalid verification code"),
    INVALID_TOKEN("4013", HttpStatus.UNAUTHORIZED,  "Token key not found"),
    INVALID_ACCESS_TOKEN("4014", HttpStatus.UNAUTHORIZED, "Invalid access token"),
    MISMATCH_TOKEN("4015", HttpStatus.UNAUTHORIZED,  "Mismatch token"),
    REFRESH_TOKEN_EXPIRED("4016", HttpStatus.UNAUTHORIZED, "Refresh token expired, please login again"),
    TOKEN_EXPIRED("4017", HttpStatus.UNAUTHORIZED, "Access Token has expired."),
    FAILED_TOKEN_PARSE("4018", HttpStatus.UNAUTHORIZED, "Failed token parse"),
    NOT_EXIST_PRE_AUTH_CREDENTIAL("4019", HttpStatus.UNAUTHORIZED, "No authentication credentials were found in the request."),

    FORBIDDEN("403", HttpStatus.FORBIDDEN, "Access denied."),
    NOT_FOUND("404", HttpStatus.NOT_FOUND, "Not found."),
    NOT_FOUND_USER("4041", HttpStatus.NOT_FOUND, "User not found."),
    NOT_FOUND_POLICY("4042", HttpStatus.NOT_FOUND, "Policy not found."),
    CONFLICT("409", HttpStatus.CONFLICT, "Conflict"),
    CONFLICT_EXIST_USER("4091", HttpStatus.CONFLICT, "User already exists."),
    CONFLICT_EXIST_EMAIL("4092", HttpStatus.CONFLICT, "Email already exists."),

    //Server Error
    INTERNAL_SERVER_ERROR("500", HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    SERVICE_UNAVAILABLE("503", HttpStatus.SERVICE_UNAVAILABLE, "Service temporarily unavailable."),
    // 보안/로그인 관련
    SECURITY_INCIDENT("601", HttpStatus.FORBIDDEN, "An unusual login attempt has been detected."),
    PASSWORD_WEAK("602", HttpStatus.BAD_REQUEST, "Password does not meet security requirements."),
    ACCOUNT_LOCKED("603", HttpStatus.FORBIDDEN, "Account has been locked due to multiple failed login attempts."),
    // 파일/데이터 관련
    FILE_NOT_FOUND("701", HttpStatus.NOT_FOUND, "File not found."),
    FILE_UPLOAD_ERROR("702", HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed."),
    DATA_CONFLICT("703", HttpStatus.CONFLICT, "Data conflict occurred."),
    // 기타
    UNKNOWN_ERROR("999", HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error occurred.");


    private final String code;
    private final HttpStatus status;
    private final String message;

    ResponseCode(String code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public String code() {
        return code;
    }

    public HttpStatus status() {
        return status;
    }

    public String message() {
        return message;
    }
}
