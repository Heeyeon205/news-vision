package com.newsvision.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {
    private final boolean success;
    private final int code;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(true, 200, "요청에 성공했습니다.", null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, 200, "요청에 성공했습니다.", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, 200, message, data);
    }

    public static <T> ApiResponse<T> fail(ErrorCode code) {
        return new ApiResponse<>(false, code.getStatus(), code.getMessage(), null);
    }
}
