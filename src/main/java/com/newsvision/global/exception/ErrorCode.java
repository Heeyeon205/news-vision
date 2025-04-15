package com.newsvision.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    UNAUTHORIZED(401, "인증이 필요합니다."),
    NOT_FOUND(404, "요청한 리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류입니다."),
    INVALID_INPUT(400, "입력값이 올바르지 않습니다.");

    private final int status;
    private final String message;
}
