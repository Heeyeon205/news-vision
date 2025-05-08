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
@Tag(name = "검색 컨트롤러", description = "뉴스 및 커뮤니티 검색 API")
public class SearchController {

    private final NewsSearchService newsSearchService;
    private final BoardSearchService boardSearchService;
    private final ObjectMapper objectMapper;
    private static final org.slf4j.Logger searchLogger = org.slf4j.LoggerFactory.getLogger("SEARCH_LOGGER");

    @Operation(
            summary = "뉴스 검색",
            description = "입력된 키워드로 뉴스 제목과 내용을 검색합니다."
    )
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
                log.error("검색 로그 직렬화 실패", e);
                e.printStackTrace();
            }
        }

        try {
            List<NewsSummaryResponse> result = newsSearchService.searchNews(keyword);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            log.error("뉴스 검색 중 오류 발생", e);
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(
            summary = "커뮤니티 검색",
            description = "입력된 키워드로 커뮤니티 게시글 내용을 검색합니다."
    )
    @GetMapping("/board")
    public ResponseEntity<ApiResponse<List<BoardResponse>>> searchBoard(@RequestParam String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            Map<String, String> logMap = new HashMap<>();
            logMap.put("type", "board");
            logMap.put("keyword", keyword);
            try {
                String jsonLog = objectMapper.writeValueAsString(logMap);
                searchLogger.info(jsonLog);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                log.warn("검색 로그 JSON 직렬화 실패", e);
            }
        }

        try {
            List<BoardResponse> result = boardSearchService.searchBoard(keyword);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            log.error("게시글 검색 중 오류 발생", e);
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(
            summary = "뉴스 제목 자동완성",
            description = "입력된 키워드를 기반으로 뉴스 제목 자동완성 추천 리스트를 반환합니다."
    )
    @GetMapping("/news/autocomplete")
    public ResponseEntity<ApiResponse<List<String>>> autocompleteNews(@RequestParam String keyword) {
        try {
            List<String> result = newsSearchService.autocompleteTitle(keyword);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            log.error("자동완성 중 오류 발생", e);
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }
}
