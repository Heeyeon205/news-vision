package com.newsvision.elasticsearch.controller;

import com.newsvision.board.controller.response.BoardResponse;
import com.newsvision.elasticsearch.service.BoardSearchService;
import com.newsvision.elasticsearch.service.NewsSearchService;
import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.news.controller.response.NewsSummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final NewsSearchService newsSearchService;
    private final BoardSearchService boardSearchService;

    @GetMapping("/news")
    public ResponseEntity<ApiResponse<List<NewsSummaryResponse>>> searchNews(@RequestParam String keyword) {
        log.info("🔍 뉴스 검색 요청 - keyword: {}", keyword); // ✅ 로그 추가
        try {
            List<NewsSummaryResponse> result = newsSearchService.searchNews(keyword);
            log.info("검색결과: {}", result);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            log.error("❌ 뉴스 검색 중 오류 발생", e); // ✅ 에러 로그 출력
            return ResponseEntity.status(500).body(ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/board")
    public ResponseEntity<ApiResponse<List<BoardResponse>>> searchBoard(@RequestParam String query) {
        return ResponseEntity.ok(ApiResponse.success(boardSearchService.searchBoard(query)));
    }
}
