package com.newsvision.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 400
    INVALID_INPUT(400, "입력값이 올바르지 않습니다."),
    INVALID_VERIFICATION_CODE(400, "인증 코드가 일치하지 않습니다."),
    BADGE_REQUIRED(400, "뱃지가 필요합니다."),
    DUPLICATE_VOTE(400, "이미 투표에 참여했습니다."),
    // 401
    UNAUTHORIZED(401, "인증이 필요합니다."),
    INVALID_ACCESS_TOKEN(401, "유효하지 않은 Access Token입니다."),
    INVALID_REFRESH_TOKEN(401, "유효하지 않은 Refresh Token입니다."),
    NOT_EXISTS_REFRESH_TOKEN(401, "저장된 Refresh Token과 일치하지 않습니다."),
    // 404,
    NOT_FOUND(404, "요청한 리소스를 찾을 수 없습니다."),
    USER_NOT_FOUND(404, "유저를 찾을 수 없습니다."),
    VERIFICATION_NOT_FOUND(404, "인증 코드가 존재하지 않습니다."),
    EMAIL_NOT_VERIFIED(404, "이메일 인증에 실패했습니다. "),
    // 409
    DUPLICATE_USERNAME(409, "사용중인 아이디입니다."),
    DUPLICATE_NICKNAME(409, "사용중인 닉네임입니다."),
    DUPLICATE_NAVER_NEWS(409, "저장된 네이버 뉴스입니다."),
    // 410
    VERIFICATION_EXPIRED(410, "인증 코드가 만료되었습니다."),
    // 500
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류입니다.");

    private final int status;
    private final String message;
}
