package com.newsvision.mypage;

import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.mypage.response.*;
import com.newsvision.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
@Tag(name = "마이페이지", description = "마이페이지 관련 API")
public class MyPageController {

    private final MypageService mypageService;
    private final UserService userService;

    @Operation(summary = "내 정보", description = "현재 로그인한 유저의 정보를 조회합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("")
    public ResponseEntity<ApiResponse<MypageInfoResponse>> userInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        MypageInfoResponse response = mypageService.getPortionUser(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "다른 유저 정보", description = "지정한 유저 ID의 정보를 조회합니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<OtherUserInfoResponse>> otherUserInfo(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long logId = userDetails != null ? userDetails.getId() : null;
        OtherUserInfoResponse response = mypageService.getPortionOtherUser(userId, logId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "내 팔로워 목록", description = "내 팔로워 목록을 조회합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/follower-list")
    public ResponseEntity<ApiResponse<List<FollowResponse>>> followerList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = userDetails.getId();
        return ResponseEntity.ok(ApiResponse.success(mypageService.getFollowers(id)));
    }

    @Operation(summary = "내 팔로잉 목록", description = "내가 팔로우한 유저 목록을 조회합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/following-list")
    public ResponseEntity<ApiResponse<List<FollowResponse>>> followingList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = userDetails.getId();
        return ResponseEntity.ok(ApiResponse.success(mypageService.getFollowing(id)));
    }

    @Operation(summary = "내 뉴스 목록", description = "내가 작성한 뉴스 글 목록을 조회합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/news-list")
    public ResponseEntity<ApiResponse<List<UserNewsListResponse>>> getMypageNewsList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long id = customUserDetails.getId();
        return ResponseEntity.ok(ApiResponse.success(mypageService.getMypageNewsList(id)));
    }

    @Operation(summary = "내 커뮤니티 글 목록", description = "내가 작성한 커뮤니티 글 목록을 조회합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/board-list")
    public ResponseEntity<ApiResponse<List<UserBoardListResponse>>> getMypageBoardList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long id = customUserDetails.getId();
        return ResponseEntity.ok(ApiResponse.success(mypageService.getMypageBoardList(id)));
    }

    @Operation(summary = "내 스크랩 목록", description = "내가 스크랩한 뉴스 목록을 조회합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/scrap-list")
    public ResponseEntity<ApiResponse<List<UserScrapListResponse>>> getMypageScrapList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long id = customUserDetails.getId();
        return ResponseEntity.ok(ApiResponse.success(mypageService.getMypageScrapList(id)));
    }

    @Operation(summary = "내 알림 목록", description = "나에게 온 알림 목록을 조회합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/notice-list")
    public ResponseEntity<ApiResponse<List<UserNoticeListResponse>>> getMyNoticeList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long id = customUserDetails.getId();
        return ResponseEntity.ok(ApiResponse.success(mypageService.getMyPageNoticeList(id)));
    }

    @Operation(summary = "다른 유저 뉴스", description = "다른 유저가 작성한 뉴스 목록을 조회합니다.")
    @GetMapping("/news-list/{userId}")
    public ResponseEntity<ApiResponse<List<UserNewsListResponse>>> getUserPageNewsList(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(mypageService.getMypageNewsList(userId)));
    }

    @Operation(summary = "다른 유저 커뮤니티 글", description = "다른 유저가 작성한 커뮤니티 글 목록을 조회합니다.")
    @GetMapping("/board-list/{userId}")
    public ResponseEntity<ApiResponse<List<UserBoardListResponse>>> getUserPageBoardList(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(mypageService.getMypageBoardList(userId)));
    }

    @Operation(summary = "다른 유저 스크랩 목록", description = "다른 유저가 스크랩한 뉴스 목록을 조회합니다.")
    @GetMapping("/scrap-list/{userId}")
    public ResponseEntity<ApiResponse<List<UserScrapListResponse>>> getUserPageScrapList(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(mypageService.getMypageScrapList(userId)));
    }

    @Operation(summary = "다른 유저 팔로워 목록", description = "다른 유저의 팔로워 목록을 조회합니다.")
    @GetMapping("/follower-list/{userId}")
    public ResponseEntity<ApiResponse<List<FollowResponse>>> getUserPageFollowerList(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(mypageService.getFollowers(userId)));
    }

    @Operation(summary = "다른 유저 팔로잉 목록", description = "다른 유저가 팔로우한 유저 목록을 조회합니다.")
    @GetMapping("/following-list/{userId}")
    public ResponseEntity<ApiResponse<List<FollowResponse>>> getUserPageFollowingList(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(mypageService.getFollowing(userId)));
    }
}
