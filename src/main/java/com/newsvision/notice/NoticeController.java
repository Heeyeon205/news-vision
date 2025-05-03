package com.newsvision.notice;

import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.jwt.JwtTokenProvider;
import com.newsvision.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
@Tag(name = "알림 컨트롤러", description = "실시간 알림 SSE 및 알림 조회 API")
public class NoticeController {

    private final NoticeService notificationService;
    private final JwtTokenProvider jwtTokenProvider;
    private final NoticeService noticeService;

    @Operation(
            summary = "실시간 알림 구독",
            description = "SSE 기반으로 클라이언트가 실시간 알림을 구독합니다. Authorization 토큰을 쿼리로 전달해야 합니다."
    )
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestParam String token) {
        log.info("subscribe token:{}", token);
        Long userId = jwtTokenProvider.getUserId(token);
        log.info("userId:{}", userId);
        return notificationService.subscribe(userId);
    }

    @Operation(
            summary = "전체 알림 목록 조회",
            description = "현재 로그인된 유저가 수신한 전체 알림을 조회합니다."
    )
    @GetMapping("/open")
    public ResponseEntity<ApiResponse<List<NoticeUserResponse>>> open(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getId();
        List<NoticeUserResponse> response = noticeService.getAllNotice(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "알림 읽음 처리",
            description = "지정된 알림 ID에 대해 읽음 처리(read check)를 수행합니다."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> open(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long id
    ) {
        Long userId = customUserDetails.getId();
        noticeService.checkRead(id);
        return ResponseEntity.ok(ApiResponse.success(id));
    }
}
