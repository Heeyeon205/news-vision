package com.newsvision.user.controller;

import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.user.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
@Tag(name = "팔로우 컨트롤러", description = "사용자 팔로우/언팔로우 기능을 제공하는 API")
public class FollowController {

    private final FollowService followService;

    @Operation(summary = "팔로우 요청", description = "지정된 사용자 ID를 팔로우합니다.")
    @PostMapping("{targetId}")
    public ResponseEntity<ApiResponse<Boolean>> follow(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long targetId
    ) {
        followService.follow(customUserDetails.getId(), targetId);
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    @Operation(summary = "팔로우 취소 요청", description = "지정된 사용자 ID에 대한 팔로우를 취소합니다.")
    @DeleteMapping("{targetId}")
    public ResponseEntity<ApiResponse<Boolean>> unFollow(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long targetId
    ) {
        followService.unFollow(customUserDetails.getId(), targetId);
        return ResponseEntity.ok(ApiResponse.success(false));
    }
}
