package com.newsvision.admin.controller;
import com.newsvision.global.exception.CustomException; // Assuming you have this
import com.newsvision.global.exception.ErrorCode;     // Assuming you have this
import com.newsvision.news.controller.response.NewsSummaryResponse; // Assuming you have this response class
import com.newsvision.news.service.NewsService;
import com.newsvision.user.entity.User; // Assuming this is your User entity

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Import for getting logged-in user
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/news") // Base path for news-related endpoints
@RequiredArgsConstructor
public class NewsListController {

    private final NewsService newsService;


    @GetMapping
    public ResponseEntity<Page<NewsSummaryResponse>> getFilteredNewsList(
            @RequestParam(value = "type", defaultValue = "recent") String type,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @PageableDefault(size = 10) Pageable pageable, // Default size 10 if not specified
            @AuthenticationPrincipal User loginUser) { // Injects the logged-in User


        Page<NewsSummaryResponse> newsPage = newsService.getFilteredArticles(type, categoryId, loginUser, pageable);
        return ResponseEntity.ok(newsPage);
    }


    @GetMapping("/top10-admin")
    public ResponseEntity<List<NewsSummaryResponse>> getTop10AdminNews() {
        List<NewsSummaryResponse> topNews = newsService.getTop10RecentNewsOnlyByAdmin();
        return ResponseEntity.ok(topNews);
    }


    @GetMapping("/creator")
    public ResponseEntity<List<NewsSummaryResponse>> getCreatorNews() {
        List<NewsSummaryResponse> creatorNews = newsService.getCreatorNewsList();
        return ResponseEntity.ok(creatorNews);
    }


    @GetMapping("/scraps/me")
    public ResponseEntity<List<NewsSummaryResponse>> getMyScraps(
            @AuthenticationPrincipal User loginUser) {

        if (loginUser == null) {

            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        List<NewsSummaryResponse> scrapList = newsService.getMyScrapList(loginUser);
        return ResponseEntity.ok(scrapList);
    }


}