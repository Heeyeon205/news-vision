package com.newsvision.news.controller;

import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.global.jwt.JwtTokenProvider;
import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.news.controller.request.NewsCreateRequest;
import com.newsvision.news.controller.request.NewsUpdateRequest;
import com.newsvision.news.controller.response.NewsResponse;
import com.newsvision.news.controller.response.NewsSummaryResponse;
import com.newsvision.news.entity.NaverNews;
import com.newsvision.news.service.NaverNewsService;
import com.newsvision.news.service.NewsService;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import com.newsvision.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {
    private final NewsService newsService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final NaverNewsService naverNewsService;

    @GetMapping("/main")
    public ResponseEntity<ApiResponse<List<NewsSummaryResponse>>> getMainNews() {
        return ResponseEntity.ok(ApiResponse.success(newsService.getTop10RecentNewsOnlyByAdmin()));
    }

    @GetMapping("/{newsId}")
    public ResponseEntity<ApiResponse<NewsResponse>> getNewsDetail(
            @PathVariable Long newsId,
            @AuthenticationPrincipal User loginUser
    ) {
        return ResponseEntity.ok(ApiResponse.success(newsService.getNewsDetail(newsId, loginUser)));
    }

    @PostMapping("/{newsId}/like")
    public ResponseEntity<ApiResponse<String>> addLike(
            @PathVariable Long newsId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        log.info("✅ 인증 객체 등록됨 - username: {}", userDetails.getUsername());
        log.info("✅ SecurityContext에 등록된 사용자: {}",
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());

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

    @GetMapping("/editorials")
    public ResponseEntity<ApiResponse<List<NewsSummaryResponse>>> getCreatorNewsList() {
        return ResponseEntity.ok(ApiResponse.success(newsService.getCreatorNewsList()));
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
            @RequestParam(required = false) Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        User user = userDetails != null ? userDetails.getUser() : null;
        return ResponseEntity.ok(ApiResponse.success(
                newsService.getFilteredArticles(type, id, user, pageable)
        ));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> createNews(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart("categoryId") String categoryIdStr,
            @RequestPart("naverNewsId") String naverNewsIdStr,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        Long userId = userDetails.getId();
        String role = userDetails.getRole();
        log.info("컨트롤러 진입완료");
        userService.validateRole(role);
        log.info("역할 검증 완료");
        Long categoryId = Long.parseLong(categoryIdStr);
        Long naverNewsId = Long.parseLong(naverNewsIdStr);
        newsService.createNews(userId, title, content, categoryId, naverNewsId, image);
        log.info("createNews 완료");
        return ResponseEntity.ok(ApiResponse.success("뉴스가 성공적으로 작성되었습니다."));
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    @PutMapping(value = "/{newsId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> updateNews(
            @PathVariable Long newsId,
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart("categoryId") String categoryIdStr,
            @RequestPart(value = "image", required = false) MultipartFile image,
            HttpServletRequest httpServletRequest
    ) {
        String token = extractToken(httpServletRequest);
        if (!jwtTokenProvider.validateToken(token)) {
            log.warn("❌ 유효하지 않은 토큰");
            return ResponseEntity.status(401).body(ApiResponse.fail(ErrorCode.UNAUTHORIZED));
        }

        Long userId = jwtTokenProvider.getUserId(token);
        String role = jwtTokenProvider.getUserRole(token);
        Long categoryId = Long.parseLong(categoryIdStr);
        log.info("📝 뉴스 수정 시도 - userId: {}, role: {}, newsId: {}", userId, role, newsId);

        try {
            newsService.updateNews(newsId, userId, title, content, categoryId, image);
            return ResponseEntity.ok(ApiResponse.success("뉴스가 성공적으로 수정되었습니다.", null));
        } catch (CustomException e) {
            log.warn("❌ 수정 중 오류 - {}", e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getStatus())
                    .body(ApiResponse.fail(e.getErrorCode()));
        } catch (Exception e) {
            log.error("🔥 뉴스 수정 중 예외 발생", e);
            return ResponseEntity.status(500).body(ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }


    @DeleteMapping("/{newsId}")
    public ResponseEntity<ApiResponse<?>> deleteNews(
            @PathVariable Long newsId,
            HttpServletRequest httpServletRequest
    ) {
        String token = extractToken(httpServletRequest);
        if (!jwtTokenProvider.validateToken(token)) {
            log.warn("❌ 유효하지 않은 토큰");
            return ResponseEntity.status(401).body(ApiResponse.fail(ErrorCode.UNAUTHORIZED));
        }

        Long userId = jwtTokenProvider.getUserId(token);
        log.info("🧹 뉴스 삭제 시도 - userId: {}, newsId: {}", userId, newsId);

        try {
            newsService.deleteNews(userId, newsId);
            return ResponseEntity.ok(ApiResponse.success("뉴스가 성공적으로 삭제되었습니다.", null));
        } catch (CustomException e) {
            log.warn("❌ 삭제 중 오류 - {}", e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getStatus())
                    .body(ApiResponse.fail(e.getErrorCode()));
        } catch (Exception e) {
            log.error("🔥 뉴스 삭제 중 예외 발생", e);
            return ResponseEntity.status(500).body(ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }
}
