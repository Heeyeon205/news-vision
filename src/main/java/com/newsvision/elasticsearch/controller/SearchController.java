package com.newsvision.elasticsearch.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsvision.board.dto.response.BoardResponse;
import com.newsvision.elasticsearch.service.BoardSearchService;
import com.newsvision.elasticsearch.service.NewsSearchService;
import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.news.dto.response.NewsSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "SearchController", description = "검색 API")
public class SearchController {

    private final NewsSearchService newsSearchService;
    private final BoardSearchService boardSearchService;
    private final ObjectMapper objectMapper;
    private static final org.slf4j.Logger searchLogger = org.slf4j.LoggerFactory.getLogger("SEARCH_LOGGER");

    @Operation(summary = "뉴스 검색", description = "뉴스 검색")
    @GetMapping("/news")
    public ResponseEntity<ApiResponse<List<NewsSummaryResponse>>> searchNews(@RequestParam String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            Map<String, String> logMap = new HashMap<>();
            logMap.put("type", "news");
            logMap.put("keyword", keyword);
            try {
                String jsonLog = objectMapper.writeValueAsString(logMap);
                searchLogger.info(jsonLog);
            } catch (JsonProcessingException e) {
                log.error("error: ", e);
                e.printStackTrace();
            }
        }

        try {
            List<NewsSummaryResponse> result = newsSearchService.searchNews(keyword);
            log.info("검색결과: {}", result);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            log.error("❌ 뉴스 검색 중 오류 발생", e);
            return ResponseEntity.status(500).body(ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "커뮤니티 검색", description = "커뮤니티 검색")
    @GetMapping("/board")
    public ResponseEntity<ApiResponse<List<BoardResponse>>> searchBoard(@RequestParam String keyword) {
        log.info("keyword: {}", keyword);
        if (keyword != null && !keyword.trim().isEmpty()) {
            Map<String, String> logMap = new HashMap<>();
            logMap.put("type", "board");
            logMap.put("keyword", keyword);
            try {
                String jsonLog = objectMapper.writeValueAsString(logMap);
                searchLogger.info(jsonLog);
            } catch (JsonProcessingException e) {
                log.warn("❌ 검색 로그 JSON 직렬화 실패", e);
            }
        }

        try {
            List<BoardResponse> result = boardSearchService.searchBoard(keyword);
            log.info("게시글 검색 결과 수: {}", result.size());
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            log.error("❌ 게시글 검색 중 오류 발생", e);
            return ResponseEntity.status(500).body(ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    // 자동완성기능 (애매함, 이상한거 검색됨, 안써도됨)
    @GetMapping("/news/autocomplete")
    public ResponseEntity<ApiResponse<List<String>>> autocompleteNews(@RequestParam String keyword) {
        try {
            List<String> result = newsSearchService.autocompleteTitle(keyword);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            log.error("❌ 자동완성 중 오류 발생", e);
            return ResponseEntity.status(500).body(ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }
}
