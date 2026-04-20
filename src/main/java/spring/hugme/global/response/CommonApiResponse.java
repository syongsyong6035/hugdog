package spring.hugme.global.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공통 응답 DTO")
public record CommonApiResponse<T>(
        @Schema(description = "커스텀 상태 코드", example = "200")
        String status,
        @Schema(description = "응답 코드명", example = "OK")
        String code,                // "OK", "TOKEN_EXPIRED"
        @Schema(description = "응답 메시지", example = "Access Token has expired.")
        String message,             // 커스텀 메세지
        @Schema(description = "응답 데이터")
        T data
) {

    // ============ 성공 응답 ============

    // 1. ResponseCode만 사용 (data 없음)
    public static <T> CommonApiResponse<T> success(ResponseCode responseCode) {
        return new CommonApiResponse<>(
                responseCode.code(),      // "200"
                responseCode.name(),      // "OK"
                responseCode.message(),   // "OK"
                null
        );
    }

    // 2. ResponseCode + 커스텀 메시지
    public static <T> CommonApiResponse<T> success(ResponseCode responseCode, String customMessage) {
        return new CommonApiResponse<>(
                responseCode.code(),      // "200"
                responseCode.name(),      // "OK"
                customMessage,            // "정상적으로 완료되었습니다."
                null
        );
    }

    // 3. ResponseCode + data
    public static <T> CommonApiResponse<T> success(ResponseCode responseCode, T data) {
        return new CommonApiResponse<>(
                responseCode.code(),      //"200"
                responseCode.name(),      //"OK"
                responseCode.message(),   //"OK"
                data
        );
    }

    // 4. ResponseCode + 커스텀 메시지 + data
    public static <T> CommonApiResponse<T> success(ResponseCode responseCode, String customMessage, T data) {
        return new CommonApiResponse<>(
                responseCode.code(),      //"200"
                responseCode.name(),      //"OK"
                customMessage,            //"로그인 성공!"
                data
        );
    }

    // ============ 실패 응답 ============

    // 1. ResponseCode만 사용
    public static <T> CommonApiResponse<T> error(ResponseCode responseCode) {
        return new CommonApiResponse<>(
                responseCode.code(),      // "4017"
                responseCode.name(),      // "TOKEN_EXPIRED"
                responseCode.message(),   // "Access Token has expired."
                null
        );
    }

    // 2. ResponseCode + 커스텀 메시지
    public static <T> CommonApiResponse<T> error(ResponseCode responseCode, String customMessage) {
        return new CommonApiResponse<>(
                responseCode.code(),      // "4017"
                responseCode.name(),      // "TOKEN_EXPIRED"
                customMessage,            // "액세스 토큰이 만료되었습니다."
                null
        );
    }

    // 3. ResponseCode + data (에러 상세 정보)
    public static <T> CommonApiResponse<T> error(ResponseCode responseCode, T data) {
        return new CommonApiResponse<>(
                responseCode.code(),      // "4017"
                responseCode.name(),      // "TOKEN_EXPIRED"
                responseCode.message(),   // "Access Token has expired."
                data
        );
    }

    // 4. ResponseCode + 커스텀 메시지 + data
    public static <T> CommonApiResponse<T> error(ResponseCode responseCode, String customMessage, T data) {
        return new CommonApiResponse<>(
                responseCode.code(),      // "4017"
                responseCode.name(),      // "TOKEN_EXPIRED"
                customMessage,            // "액세스 토큰이 만료되었습니다. 다시 로그인해주세요."
                data
        );
    }
}