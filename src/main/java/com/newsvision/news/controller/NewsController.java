package com.newsvision.news.controller;

import com.newsvision.global.response.ApiResponse;
import com.newsvision.news.controller.response.NewsResponse;
import com.newsvision.news.controller.response.NewsSummaryResponse;
import com.newsvision.news.entity.NewsLike;
import com.newsvision.news.service.NewsService;
import com.newsvision.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class NewsController {
    private final NewsService newsService;

    @GetMapping("/main")
    public ApiResponse<List<NewsSummaryResponse>> getMainNews() {
        return ApiResponse.success(newsService.getTop10RecentNews());
    }

    @GetMapping("/news/{newsId}")
    public ApiResponse<NewsResponse> getNewsDetail(
            @PathVariable Long newsId,
            @AuthenticationPrincipal User loginUser
    ) {
        return ApiResponse.success(newsService.getNewsDetail(newsId, loginUser));
    }

    @PostMapping("/news/{newsId}/like")
    public ApiResponse<String> toggleLike(
            @PathVariable Long newsId,
            @AuthenticationPrincipal User loginUser
    ) {
        boolean liked = newsService.toggleLike(newsId, loginUser);
        return liked ?
                ApiResponse.success("좋아요를 추가했습니다.") :
                ApiResponse.success("좋아요를 취소했습니다.");
    }


    @PostMapping("/news/{newsId}/scrap")
    public ApiResponse<String> toggleScrap(
            @PathVariable Long newsId,
            @AuthenticationPrincipal User loginUser
    ) {
        boolean scrapped = newsService.toggleScrap(newsId, loginUser);
        return scrapped ?
                ApiResponse.success("스크랩을 추가했습니다.") :
                ApiResponse.success("스크랩을 취소했습니다.");
    }

    @GetMapping("/scraps")
    public ApiResponse<List<NewsSummaryResponse>> getMyScrapList(
            @AuthenticationPrincipal User loginUser
    ) {
        return ApiResponse.success(newsService.getMyScrapList(loginUser));
    }


}
