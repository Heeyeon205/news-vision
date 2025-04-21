package com.newsvision.news.controller;

import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.news.controller.request.NaverNewsSaveRequest;
import com.newsvision.news.dto.response.NaverNewsInfoResponse;
import com.newsvision.news.service.NaverNewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/naver-news")
public class NaverNewsController {
    private final NaverNewsService naverNewsService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<NaverNewsInfoResponse>>> searchNews(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int display) {
        Long id = customUserDetails.getId();
        List<NaverNewsInfoResponse> result = naverNewsService.searchNews(query, display);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<Long>> saveNews(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody NaverNewsSaveRequest request) {
        Long id = customUserDetails.getId();
        Long savedId = naverNewsService.saveNaverNews(request);
        return ResponseEntity.ok(ApiResponse.success(savedId));
    }
}
