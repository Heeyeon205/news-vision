package com.newsvision.news.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.newsvision.global.exception.ApiResponse;
import com.newsvision.news.controller.response.GptNewsSummaryResponse;
import com.newsvision.news.controller.response.NewsSummaryResponse;
import com.newsvision.news.entity.NaverNews;
import com.newsvision.news.entity.News;
import com.newsvision.news.repository.GptNewsRepository;
import com.newsvision.news.repository.NewsRepository;
import com.newsvision.news.service.GptSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gpt-news")
public class GptNewsController {
    private final GptSummaryService gptSummaryService;
    private final NewsRepository newsRepository;
    private final GptNewsRepository gptNewsRepository;

    @GetMapping("/main-summary")
    public ResponseEntity<ApiResponse<List<GptNewsSummaryResponse>>> getMainNewsSummaries() {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        List<News> newsList = newsRepository.findTopNewsByAdminOnly(threeDaysAgo, PageRequest.of(0, 10));

        List<GptNewsSummaryResponse> response = newsList.stream()
                .map(news -> gptNewsRepository.findByNewsId(news.getId())
                        .map(gptNews -> new GptNewsSummaryResponse(gptNews.getTitle(), gptNews.getSummary()))
                        .orElse(new GptNewsSummaryResponse(news.getTitle(), "요약이 존재하지 않습니다."))
                )
                .toList();

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
