package com.newsvision.news.controller;

import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.news.controller.request.NaverNewsSaveRequest;
import com.newsvision.news.dto.response.NaverNewsInfoResponse;
import com.newsvision.news.service.NaverNewsService;
import com.newsvision.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/naver-news")
public class NaverNewsController {
    private final NaverNewsService naverNewsService;
    private final UserService userService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<NaverNewsInfoResponse>>> searchNews(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int display) {
        userService.validateRole(customUserDetails.getRole());
        List<NaverNewsInfoResponse> result = naverNewsService.searchNews(query, display);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<Long>> saveNews(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody NaverNewsSaveRequest request) {
        userService.validateRole(customUserDetails.getRole());
        Long savedId = naverNewsService.saveNaverNews(request);
        return ResponseEntity.ok(ApiResponse.success(savedId));
    }
}
