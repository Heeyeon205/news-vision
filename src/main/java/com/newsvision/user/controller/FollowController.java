package com.newsvision.user.controller;

import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.global.jwt.JwtTokenProvider;
import com.newsvision.global.jwt.JwtUtil;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.user.dto.request.JoinUserRequest;
import com.newsvision.user.dto.request.UpdatePasswordRequest;
import com.newsvision.user.dto.response.*;
import com.newsvision.user.entity.User;
import com.newsvision.user.service.FollowService;
import com.newsvision.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping("{targetId}")
    public ResponseEntity<ApiResponse<Boolean>> follow(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long targetId
    ) {
        followService.follow(customUserDetails.getId(), targetId);
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    @DeleteMapping("{targetId}")
    public ResponseEntity<ApiResponse<Boolean>> unFollow(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long targetId
    ) {
        followService.unFollow(customUserDetails.getId(), targetId);
        return ResponseEntity.ok(ApiResponse.success(false));
    }
}
