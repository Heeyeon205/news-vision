package com.newsvision.elasticsearch.controller;

import com.newsvision.elasticsearch.dto.PopularKeywordResponse;
import com.newsvision.elasticsearch.service.PopularKeywordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/popular")
@Tag(name = "인기 검색어 컨트롤러", description = "3일 이내의 인기 검색어 조회 API")
public class PopularKeywordController {

    private final PopularKeywordService popularKeywordService;

    @Operation(summary = "타입별 인기 검색어 조회", description = "뉴스 또는 커뮤니티 유형에 따른 인기 검색어 10개를 반환합니다. type에는 'news' 또는 'board'를 사용합니다.")
    @GetMapping("/{type}")
    public ResponseEntity<List<PopularKeywordResponse>> getPopularKeywords(@PathVariable String type) {
        log.info("✅ 인기 검색어 요청 type: {}", type);
        List<PopularKeywordResponse> result = popularKeywordService.getPopularKeywordsByType(type);
        log.info("📦 인기 검색어 결과 개수: {}", result.size());
        return ResponseEntity.ok(result);
    }
}
