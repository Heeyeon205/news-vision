package com.newsvision.news.controller;

import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.news.controller.request.NaverNewsSaveRequest;
import com.newsvision.news.controller.response.NewsResponse;
import com.newsvision.news.controller.response.NewsSummaryResponse;
import com.newsvision.news.dto.response.NewsDetailInfoResponse;
import com.newsvision.news.entity.News;
import com.newsvision.news.service.NaverNewsService;
import com.newsvision.news.service.NewsService;
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
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {
    private final NewsService newsService;
    private final UserService userService;
    private final NaverNewsService naverNewsService;

    @GetMapping("/main")
    public ResponseEntity<ApiResponse<List<NewsSummaryResponse>>> getMainNews() {
        return ResponseEntity.ok(ApiResponse.success(newsService.getTop10RecentNewsOnlyByAdmin()));
    }

    @GetMapping("/{newsId}")
    public ResponseEntity<ApiResponse<NewsResponse>> getNewsDetail(@PathVariable Long newsId) {
        News news = newsService.findByNewsId(newsId);
        User user = userService.findByUserId(news.getUser().getId());
        return ResponseEntity.ok(ApiResponse.success(newsService.getNewsDetail(newsId, user)));
    }

    @PostMapping("/{newsId}/like")
    public ResponseEntity<ApiResponse<String>> addLike(
            @PathVariable Long newsId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        newsService.addLike(newsId, userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success("좋아요를 추가했습니다."));
    }


    @DeleteMapping("/{newsId}/like")
    public ResponseEntity<ApiResponse<String>> removeLike(
            @PathVariable Long newsId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        newsService.removeLike(newsId, userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success("좋아요를 취소했습니다."));
    }


    @PostMapping("/{newsId}/scrap")
    public ResponseEntity<ApiResponse<String>> addScrap(
            @PathVariable Long newsId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        newsService.addScrap(newsId, userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success("스크랩을 추가했습니다."));
    }

    @DeleteMapping("/{newsId}/scrap")
    public ResponseEntity<ApiResponse<String>> removeScrap(
            @PathVariable Long newsId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        newsService.removeScrap(newsId, userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success("스크랩을 취소했습니다."));
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
        if (!Objects.equals(news.getUser().getId(), userDetails.getId())) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
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
    public ResponseEntity<ApiResponse<?>> deleteNews(
            @PathVariable Long newsId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Long userId = customUserDetails.getId();
            newsService.deleteNews(userId, newsId);
            return ResponseEntity.ok(ApiResponse.success("뉴스가 성공적으로 삭제되었습니다."));
    }
}
