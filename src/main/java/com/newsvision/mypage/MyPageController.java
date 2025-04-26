package com.newsvision.mypage;

import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.mypage.response.*;
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
    private final MypageService mypageService;

    @GetMapping({"", "/"})
    public ResponseEntity<ApiResponse<MypageInfoResponse>> userInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        MypageInfoResponse response = mypageService.getPortionUser(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/follower-list")
    public ResponseEntity<ApiResponse<List<FollowResponse>>> followerList(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = userDetails.getId();
        List<FollowResponse> response = mypageService.getFollowers(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/following-list")
    public ResponseEntity<ApiResponse<List<FollowResponse>>> followingList(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = userDetails.getId();
        List<FollowResponse> response = mypageService.getFollowing(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/board-list")
    public ResponseEntity<ApiResponse<List<UserBoardListResponse>>> getMypageBoardList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long id = customUserDetails.getId();
        List<UserBoardListResponse> response = mypageService.getMypageBoardList(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/news-list")
    public ResponseEntity<ApiResponse<List<UserNewsListResponse>>> getMypageNewsList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long id = customUserDetails.getId();
        List<UserNewsListResponse> response = mypageService.getMypageNewsList(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/scrap-list")
    public ResponseEntity<ApiResponse<List<UserScrapListResponse>>> getMypageScrapList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long id = customUserDetails.getId();
        List<UserScrapListResponse> response = mypageService.getMypageScrapList(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/notice-list")
    public ResponseEntity<ApiResponse<List<UserNoticeListResponse>>> getMyNoticeList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Long id = customUserDetails.getId();
        List<UserNoticeListResponse> response = mypageService.getMyPageNoticeList(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}