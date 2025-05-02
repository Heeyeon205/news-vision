package com.newsvision.mypage;

import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.mypage.response.*;
import com.newsvision.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
@Tag(name = "MyPageController", description = "MyPage API")
public class MyPageController {
    private final MypageService mypageService;
    private final UserService userService;

    @Operation(summary = "내 정보 보기", description = "내 정보 보기")
    @GetMapping({"", "/"})
    public ResponseEntity<ApiResponse<MypageInfoResponse>> userInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        MypageInfoResponse response = mypageService.getPortionUser(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "타 유저 정보 보기", description = "타 유저 정보 보기")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<OtherUserInfoResponse>> otherUserInfo(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long logId = userDetails != null ? userDetails.getId() : null;
        OtherUserInfoResponse response = mypageService.getPortionOtherUser(userId, logId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "내 팔로워 보기", description = "내 팔로워 보기")
    @GetMapping("/follower-list")
    public ResponseEntity<ApiResponse<List<FollowResponse>>> followerList(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = userDetails.getId();
        List<FollowResponse> response = mypageService.getFollowers(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "내 팔로잉 보기", description = "내 팔로잉 보기")
    @GetMapping("/following-list")
    public ResponseEntity<ApiResponse<List<FollowResponse>>> followingList(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = userDetails.getId();
        List<FollowResponse> response = mypageService.getFollowing(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "내가 작성한 뉴스 보기", description = "내가 작성한 뉴스 보기")
    @GetMapping("/news-list")
    public ResponseEntity<ApiResponse<List<UserNewsListResponse>>> getMypageNewsList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long id = customUserDetails.getId();
        List<UserNewsListResponse> response = mypageService.getMypageNewsList(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "내가 작성한 커뮤니티 글 보기", description = "내가 작성한 커뮤니티 글 보기")
    @GetMapping("/board-list")
    public ResponseEntity<ApiResponse<List<UserBoardListResponse>>> getMypageBoardList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long id = customUserDetails.getId();
        List<UserBoardListResponse> response = mypageService.getMypageBoardList(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "내 스크랩 목록 보기", description = "내 스크랩 목록 보기")
    @GetMapping("/scrap-list")
    public ResponseEntity<ApiResponse<List<UserScrapListResponse>>> getMypageScrapList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long id = customUserDetails.getId();
        List<UserScrapListResponse> response = mypageService.getMypageScrapList(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    @Operation(summary = "내 알람 목록 보기", description = "내 알람 목록 보기")
    @GetMapping("/notice-list")
    public ResponseEntity<ApiResponse<List<UserNoticeListResponse>>> getMyNoticeList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Long id = customUserDetails.getId();
        List<UserNoticeListResponse> response = mypageService.getMyPageNoticeList(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "타 유저 작성 뉴스 목록 ", description = "타 유저 작성 뉴스 목록")
    @GetMapping("/news-list/{userId}")
    public ResponseEntity<ApiResponse<List<UserNewsListResponse>>> getUserPageNewsList(
            @PathVariable Long userId) {
        log.info("뉴스리스트 userId = {}", userId);
        List<UserNewsListResponse> response = mypageService.getMypageNewsList(userId);
        log.info("뉴스리스트 response = {}", response);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "타 유저 작성 커뮤니티 글 목록", description = "타 유저 작성 커뮤니티 글 목록")
    @GetMapping("/board-list/{userId}")
    public ResponseEntity<ApiResponse<List<UserBoardListResponse>>> getUserPageBoardList(
            @PathVariable Long userId) {
        List<UserBoardListResponse> response = mypageService.getMypageBoardList(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "타 유저가 스크랩한 뉴스 목록", description = "타 유저가 스크랩한 뉴스 목록")
    @GetMapping("/scrap-list/{userId}")
    public ResponseEntity<ApiResponse<List<UserScrapListResponse>>> getUserPageScrapList(
            @PathVariable Long userId) {
        List<UserScrapListResponse> response = mypageService.getMypageScrapList(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "타 유저 팔로워 리스트 목록", description = "타 유저 팔로워 리스트 목록")
    @GetMapping("/follower-list/{userId}")
    public ResponseEntity<ApiResponse<List<FollowResponse>>> getUserPagefollowerList(
            @PathVariable Long userId) {
        List<FollowResponse> response = mypageService.getFollowers(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "타 유저 팔로잉 리스트 목록", description = "타 유저 팔로잉 리스트 목록")
    @GetMapping("/following-list/{userId}")
    public ResponseEntity<ApiResponse<List<FollowResponse>>> getUserPagefollowingList(
           @PathVariable Long userId) {
        List<FollowResponse> response = mypageService.getFollowing(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}