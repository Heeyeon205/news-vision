package com.newsvision.news.controller;

import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.news.dto.request.NaverNewsSaveRequest;
import com.newsvision.news.dto.response.NaverNewsInfoResponse;
import com.newsvision.news.service.NaverNewsService;
import com.newsvision.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "NaverNewsController", description = "네이버 뉴스 API")
public class NaverNewsController {
    private final NaverNewsService naverNewsService;
    private final UserService userService;

    @Operation(summary = "네이버 뉴스 검색", description = "네이버 뉴스 검색")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<NaverNewsInfoResponse>>> searchNews(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int display) {
        userService.validateRole(customUserDetails.getRole());
        List<NaverNewsInfoResponse> result = naverNewsService.searchNews(query, display);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "네이버 뉴스 불러와 저장", description = "네이버 뉴스 불러와 저장")
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<Long>> saveNews(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody NaverNewsSaveRequest request) {
        userService.validateRole(customUserDetails.getRole());
        Long savedId = naverNewsService.saveNaverNews(request);
        return ResponseEntity.ok(ApiResponse.success(savedId));
    }
}
