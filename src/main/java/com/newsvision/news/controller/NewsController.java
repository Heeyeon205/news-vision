package com.newsvision.news.controller;

import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.news.dto.request.NaverNewsSaveRequest;
import com.newsvision.news.dto.response.*;
import com.newsvision.news.entity.News;
import com.newsvision.news.service.NaverNewsService;
import com.newsvision.news.service.NewsLikeService;
import com.newsvision.news.service.NewsService;
import com.newsvision.news.service.ScrapService;
import com.newsvision.poll.service.PollService;
import com.newsvision.user.entity.User;
import com.newsvision.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {
    private final NewsService newsService;
    private final UserService userService;
    private final NaverNewsService naverNewsService;
    private final NewsLikeService newsLikeService;

    @GetMapping("/main")
    public ResponseEntity<ApiResponse<NewsMainDataResponse>> getMainNews() {
        return ResponseEntity.ok(ApiResponse.success(newsService.getNewsMain()));
    }

    @GetMapping("/{newsId}")
    public ResponseEntity<ApiResponse<NewsResponse>> getNewsDetail(
            @PathVariable Long newsId,
    @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = (userDetails != null) ? userDetails.getId() : null;
        return ResponseEntity.ok(ApiResponse.success(newsService.getNewsDetail(newsId, userId)));
    }

    @PostMapping("/{newsId}/like")
    public ResponseEntity<ApiResponse<NewsLikeResponse>> addLike(
            @PathVariable Long newsId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        News news = newsService.findByNewsId(newsId);
        newsService.addLike(newsId, userDetails.getId());
        int likeCount = newsLikeService.findLikeCountByNews(news);
        return ResponseEntity.ok(ApiResponse.success(new NewsLikeResponse(likeCount, true)));
    }

    @DeleteMapping("/{newsId}/like")
    public ResponseEntity<ApiResponse<NewsLikeResponse>> removeLike(
            @PathVariable Long newsId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        News news = newsService.findByNewsId(newsId);
        newsService.removeLike(newsId, userDetails.getId());
        int likeCount = newsLikeService.findLikeCountByNews(news);
        return ResponseEntity.ok(ApiResponse.success(new NewsLikeResponse(likeCount, false)));
    }

    @PostMapping("/{newsId}/scrap")
    public ResponseEntity<ApiResponse<NewsScrapResponse>> addScrap(
            @PathVariable Long newsId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        newsService.addScrap(newsId, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(new NewsScrapResponse(true)));
    }

    @DeleteMapping("/{newsId}/scrap")
    public ResponseEntity<ApiResponse<NewsScrapResponse>> removeScrap(
            @PathVariable Long newsId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        newsService.removeScrap(newsId, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(new NewsScrapResponse(false)));
    }

    @GetMapping("/scraps")
    public ResponseEntity<ApiResponse<List<NewsSummaryResponse>>> getMyScrapList(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(ApiResponse.success(newsService.getMyScrapList(userDetails.getUser())));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<NewsSummaryResponse>>> getAllNewsPaged(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(newsService.getNewsListByCreatedAt(pageable)));
    }

    @GetMapping("/article")
    public ResponseEntity<ApiResponse<Page<NewsSummaryResponse>>> getFilteredArticles(
            @RequestParam(defaultValue = "recent") String type,
            @RequestParam(required = false) Long categoryId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        User user = userDetails != null ? userDetails.getUser() : null;
        return ResponseEntity.ok(ApiResponse.success(
                newsService.getFilteredArticles(type, categoryId, user, pageable)
        ));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> createNews(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart("categoryId") String categoryId,
            @RequestPart("naverTitle") String naverTitle,
            @RequestPart("naverLink") String naverLink,
            @RequestPart("naverPubDate") String naverPubDate,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        Long userId = userDetails.getId();
        String role = userDetails.getRole();
        userService.validateRole(role);
        Long categoriesId = Long.parseLong(categoryId);
        Long naverNewsId = naverNewsService.saveNaverNews(new NaverNewsSaveRequest(naverTitle, naverLink, naverPubDate));
        newsService.createNews(userId, title, content, categoriesId, naverNewsId, image);
        return ResponseEntity.ok(ApiResponse.success("뉴스가 성공적으로 작성되었습니다."));
    }

    @GetMapping(value = "/update/{newsId}")
    public ResponseEntity<ApiResponse<NewsDetailInfoResponse>> updateForm(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long newsId
    ) {
        News news = newsService.findByNewsId(newsId);
        userService.matchUserId(userDetails.getId(),news.getUser().getId());
        NewsDetailInfoResponse response = newsService.newsInfo(newsId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping(value = "/{newsId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> updateNews(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long newsId,
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart("categoryId") String categoryId,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        Long userId = userDetails.getId();
        String role = userDetails.getRole();
        userService.validateRole(role);
        Long categoriesId = Long.parseLong(categoryId);

        newsService.updateNews(newsId, userId, title, content, categoriesId, image);
        return ResponseEntity.ok(ApiResponse.success("뉴스가 성공적으로 수정되었습니다."));
    }

    @DeleteMapping("/{newsId}")
    public ResponseEntity<ApiResponse<String>> deleteNews(
            @PathVariable Long newsId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
            Long userId = customUserDetails.getId();
            News news = newsService.findByNewsId(newsId);
            naverNewsService.deleteNaverNews(news.getNaverNews().getId());
            newsService.deleteNews(userId, newsId);
            return ResponseEntity.ok(ApiResponse.success("뉴스가 성공적으로 삭제되었습니다."));
    }
}
