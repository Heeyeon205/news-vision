package com.newsvision.mypage.controller;

import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.mypage.dto.response.MypageInfoResponse;
import com.newsvision.mypage.service.MypageService;
import com.newsvision.user.dto.response.FollowResponse;
import com.newsvision.user.service.FollowService;
import com.newsvision.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final UserService userService;
    private final FollowService followService;
    private final MypageService mypageService;

    @GetMapping({"", "/"})
    public ResponseEntity<ApiResponse<MypageInfoResponse>> userInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        MypageInfoResponse response = mypageService.getPortionUser(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/follower")
    public ResponseEntity<ApiResponse<List<FollowResponse>>> followerList(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = userDetails.getId();
        List<FollowResponse> response = followService.getFollowers(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/following")
    public ResponseEntity<ApiResponse<List<FollowResponse>>> followingList(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = userDetails.getId();
        List<FollowResponse> response = followService.getFollowing(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}