package com.newsvision.news.controller;

import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.global.jwt.JwtTokenProvider;
import com.newsvision.global.response.ApiResponse;
import com.newsvision.news.controller.response.NewsResponse;
import com.newsvision.news.controller.response.NewsSummaryResponse;
import com.newsvision.news.service.NewsService;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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

//    @PostMapping("/create")
//    public ResponseEntity<?> createNews(
//            @RequestHeader("Authorization") String authHeader,
//            @RequestBody NewsRequestDto requestDto) {
//
//        try {
//            String token = authHeader.replace("Bearer ", "");
//            String role = jwtTokenProvider.getUserRole(token);
//            Long userId = jwtTokenProvider.getUserId(token);
//
//            if (!role.equals("ROLE_ADMIN") && !role.equals("ROLE_CREATOR")) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                        .body("작성 권한이 없습니다.");
//            }
//
//            newsService.createNews(requestDto, userId);
//            return ResponseEntity.ok("뉴스 작성 완료");
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body("뉴스 작성 실패: " + e.getMessage());
//        }
//    }



}
