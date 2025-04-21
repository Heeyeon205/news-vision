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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

//    @PostMapping("/news/{newsId}/like")
//    public ResponseEntity<ApiResponse<String>> addLike(
//            @PathVariable Long newsId,
//            @AuthenticationPrincipal User loginUser
//    ) {
//        newsService.addLike(newsId, loginUser);
//        return ResponseEntity.ok(ApiResponse.success("좋아요를 추가했습니다."));
//    }

    @PostMapping("/{newsId}/like")
    public ResponseEntity<ApiResponse<String>> addLike(
            @PathVariable Long newsId
            //@AuthenticationPrincipal User loginUser
    ) {
        // 💡 테스트용 유저 직접 주입
        User loginUser = userRepository.findByUsername("user1@test.com")
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        newsService.addLike(newsId, loginUser);
        return ResponseEntity.ok(ApiResponse.success("좋아요 추가됨"));
    }

//    @DeleteMapping("/news/{newsId}/like")
//    public ResponseEntity<ApiResponse<String>> removeLike(
//            @PathVariable Long newsId,
//            @AuthenticationPrincipal User loginUser
//    ) {
//        newsService.removeLike(newsId, loginUser);
//        return ResponseEntity.ok(ApiResponse.success("좋아요를 취소했습니다."));
//    }

    @DeleteMapping("/{newsId}/like")
    public ResponseEntity<ApiResponse<String>> removeLike(
            @PathVariable Long newsId
            //@AuthenticationPrincipal User loginUser
    ) {
        User loginUser = userRepository.findByUsername("user1@test.com")
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        newsService.removeLike(newsId, loginUser);
        return ResponseEntity.ok(ApiResponse.success("좋아요를 취소했습니다."));
    }

    @PostMapping("/{newsId}/scrap")
    public ResponseEntity<ApiResponse<String>> addScrap(
            @PathVariable Long newsId,
            @AuthenticationPrincipal User loginUser
    ) {
        newsService.addScrap(newsId, loginUser);
        return ResponseEntity.ok(ApiResponse.success("스크랩을 추가했습니다."));
    }

    @DeleteMapping("/{newsId}/scrap")
    public ResponseEntity<ApiResponse<String>> removeScrap(
            @PathVariable Long newsId,
            @AuthenticationPrincipal User loginUser
    ) {
        newsService.removeScrap(newsId, loginUser);
        return ResponseEntity.ok(ApiResponse.success("스크랩을 취소했습니다."));
    }


    @GetMapping("/scraps")
    public ResponseEntity<ApiResponse<List<NewsSummaryResponse>>> getMyScrapList(
            @AuthenticationPrincipal User loginUser
    ) {
        return ResponseEntity.ok(ApiResponse.success(newsService.getMyScrapList(loginUser)));
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
            @AuthenticationPrincipal User loginUser,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                newsService.getFilteredArticles(type, id, loginUser, pageable)
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createNews(
            @RequestBody NewsCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        String role = userDetails.getRole();
        userService.validateRole(role);
        newsService.createNews(userId, request);
        return ResponseEntity.ok(ApiResponse.success("뉴스가 성공적으로 작성되었습니다."));
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    @PutMapping("/{newsId}")
    public ResponseEntity<ApiResponse<?>> updateNews(
            @PathVariable Long newsId,
            @RequestBody NewsUpdateRequest request,
            HttpServletRequest httpServletRequest
    ) {
        String token = extractToken(httpServletRequest);
        if (!jwtTokenProvider.validateToken(token)) {
            log.warn("❌ 유효하지 않은 토큰");
            return ResponseEntity.status(401).body(ApiResponse.fail(ErrorCode.UNAUTHORIZED));
        }

        Long userId = jwtTokenProvider.getUserId(token);
        String role = jwtTokenProvider.getUserRole(token);

        log.info("📝 뉴스 수정 시도 - userId: {}, role: {}, newsId: {}", userId, role, newsId);

        request.setNewsId(newsId); // 🔑 PathVariable → Request로 전달

        try {
            newsService.updateNews(userId, request);
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
