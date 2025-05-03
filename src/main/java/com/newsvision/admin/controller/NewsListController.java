package com.newsvision.admin.controller;

import com.newsvision.admin.service.NewsListService;
import com.newsvision.global.exception.ApiResponse;
import com.newsvision.news.dto.response.NewsResponse;
import com.newsvision.news.service.NewsService;
import com.newsvision.poll.service.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/news") // 관리자용 뉴스 경로
@RequiredArgsConstructor
public class NewsListController {
    private final NewsListService newsListService; // 목록 조회용 서비스
    private final NewsService newsService;       // 개별 뉴스 처리용 서비스

//    @GetMapping
//    public ResponseEntity<ApiResponse<List<NewsResponse>>> getNewsList(
//            @AuthenticationPrincipal UserDetails userDetails) {
//        if (userDetails == null || !userDetails.getAuthorities().stream()
//                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
//            return ResponseEntity.status(403).body(null);
//        }
//        List<NewsResponse> newsList = newsListService.getAllNews();
//        return ResponseEntity.ok(ApiResponse.success(newsList));
//    }
//
//    @GetMapping("/max")
//    public ResponseEntity<ApiResponse<List<NewsResponse>>> getAllNews(
//            @AuthenticationPrincipal UserDetails userDetails) {
//
//        if (userDetails == null || !userDetails.getAuthorities().stream()
//                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
//            return ResponseEntity.status(403).body(null);
//        }
//
//        List<NewsResponse> newsList = newsListService.getMaxAllNews();
//        return ResponseEntity.ok(ApiResponse.success(newsList));
//    }
//
//    @DeleteMapping("/delete/{newsId}")
//    public ResponseEntity<ApiResponse<String>> deleteNews(
//            @PathVariable Long newsId,
//            @RequestParam Long userId,
//            @AuthenticationPrincipal UserDetails userDetails) {
//
//        if (userDetails == null || !userDetails.getAuthorities().stream()
//                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
//            return ResponseEntity.status(403).body(null);
//        }
//
//        newsService.deleteNews(userId, newsId);
//        return ResponseEntity.ok(ApiResponse.success("뉴스(ID: " + newsId + ") 삭제 완료"));
//    }
}