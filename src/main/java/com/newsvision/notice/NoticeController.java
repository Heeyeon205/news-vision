package com.newsvision.notice;


import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.jwt.JwtTokenProvider;
import com.newsvision.global.security.CustomUserDetails;
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
public class NoticeController {
    private final NoticeService notificationService;
    private final JwtTokenProvider jwtTokenProvider;
    private final NoticeService noticeService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestParam String token) {
        log.info("subscribe token:{}", token);
        Long userId = jwtTokenProvider.getUserId(token);
        log.info("userId:{}", userId);
        return notificationService.subscribe(userId);
    }

    @GetMapping("/open")
    public ResponseEntity<ApiResponse<List<NoticeUserResponse>>> open(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getId();
       List<NoticeUserResponse> response = noticeService.getAllNotice(userId);
       return ResponseEntity.ok(ApiResponse.success(response));
    }

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
