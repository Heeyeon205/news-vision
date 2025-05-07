package com.newsvision.notice.controller;

import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.jwt.JwtTokenProvider;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.notice.service.NoticeService;
import com.newsvision.notice.dto.response.NoticeUserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
@Tag(name = "알림 컨트롤러", description = "알림 관련 API")
public class NoticeController {

    private final NoticeService notificationService;
    private final JwtTokenProvider jwtTokenProvider;
    private final NoticeService noticeService;

    @Operation(
            summary = "알림 구독",
            description = "SSE 방식으로 알림을 실시간으로 구독합니다. `token` 파라미터에 JWT를 전달해야 합니다."
    )
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
            @Parameter(description = "JWT 토큰", required = true) @RequestParam String token) {
        log.info("subscribe token:{}", token);
        Long userId = jwtTokenProvider.getUserId(token);
        log.info("userId:{}", userId);
        return notificationService.subscribe(userId);
    }

    @Operation(
            summary = "내 알림 목록 보기",
            description = "로그인한 사용자의 전체 알림 목록을 반환합니다. 페이지네이션이 적용됩니다."
    )
    @GetMapping("/open")
    public ResponseEntity<ApiResponse<Page<NoticeUserResponse>>> open(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(hidden = true)
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<NoticeUserResponse> response = noticeService.getAllNotice(customUserDetails.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "알림 단건 읽음 처리",
            description = "알림 ID를 통해 해당 알림을 읽음 처리합니다."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> open(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "알림 ID", required = true, example = "12") @PathVariable Long id
    ) {
        Long userId = customUserDetails.getId();
        noticeService.checkRead(id);
        return ResponseEntity.ok(ApiResponse.success(id));
    }
}
