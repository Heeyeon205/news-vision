package com.newsvision.mypage.controller;

import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.mypage.service.MypageService;
import com.newsvision.mypage.dto.response.*;
import com.newsvision.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<ApiResponse<Page<FollowResponse>>> followerList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
    @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long id = userDetails.getId();
        Page<FollowResponse> response = mypageService.getFollowers(id, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "내 팔로잉 보기", description = "내 팔로잉 보기")
    @GetMapping("/following-list")
    public ResponseEntity<ApiResponse<Page<FollowResponse>>> followingList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
    @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long id = userDetails.getId();
        Page<FollowResponse> response = mypageService.getFollowing(id, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "내가 작성한 뉴스 보기", description = "내가 작성한 뉴스 보기")
    @GetMapping("/news-list")
    public ResponseEntity<ApiResponse<Page<UserNewsListResponse>>> getMypageNewsList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long id = customUserDetails.getId();
        Page<UserNewsListResponse> response = mypageService.getMypageNewsList(id, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "내가 작성한 커뮤니티 글 보기", description = "내가 작성한 커뮤니티 글 보기")
    @GetMapping("/board-list")
    public ResponseEntity<ApiResponse<Page<UserBoardListResponse>>> getMypageBoardList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long id = customUserDetails.getId();
        Page<UserBoardListResponse> response = mypageService.getMypageBoardList(id, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "내 스크랩 목록 보기", description = "내 스크랩 목록 보기")
    @GetMapping("/scrap-list")
    public ResponseEntity<ApiResponse<Page<UserScrapListResponse>>> getMypageScrapList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
     @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long id = customUserDetails.getId();
        Page<UserScrapListResponse> response = mypageService.getMypageScrapList(id, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "내 알람 목록 보기", description = "내 알람 목록 보기")
    @GetMapping("/notice-list")
    public ResponseEntity<ApiResponse<Page<UserNoticeListResponse>>> getMyNoticeList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long id = customUserDetails.getId();
        Page<UserNoticeListResponse> response = mypageService.getMyPageNoticeList(id, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "타 유저 작성 뉴스 목록 ", description = "타 유저 작성 뉴스 목록")
    @GetMapping("/news-list/{userId}")
    public ResponseEntity<ApiResponse<Page<UserNewsListResponse>>> getUserPageNewsList(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<UserNewsListResponse> response = mypageService.getMypageNewsList(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "타 유저 작성 커뮤니티 글 목록", description = "타 유저 작성 커뮤니티 글 목록")
    @GetMapping("/board-list/{userId}")
    public ResponseEntity<ApiResponse<Page<UserBoardListResponse>>> getUserPageBoardList(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
            ) {
        Page<UserBoardListResponse> response = mypageService.getMypageBoardList(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "타 유저가 스크랩한 뉴스 목록", description = "타 유저가 스크랩한 뉴스 목록")
    @GetMapping("/scrap-list/{userId}")
    public ResponseEntity<ApiResponse<Page<UserScrapListResponse>>> getUserPageScrapList(
            @PathVariable Long userId,
    @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<UserScrapListResponse> response = mypageService.getMypageScrapList(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "타 유저 팔로워 리스트 목록", description = "타 유저 팔로워 리스트 목록")
    @GetMapping("/follower-list/{userId}")
    public ResponseEntity<ApiResponse<Page<FollowResponse>>> getUserPagefollowerList(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<FollowResponse> response = mypageService.getFollowers(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "타 유저 팔로잉 리스트 목록", description = "타 유저 팔로잉 리스트 목록")
    @GetMapping("/following-list/{userId}")
    public ResponseEntity<ApiResponse<Page<FollowResponse>>> getUserPagefollowingList(
           @PathVariable Long userId,
           @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<FollowResponse> response = mypageService.getFollowing(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}