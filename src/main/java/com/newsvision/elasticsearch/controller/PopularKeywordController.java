package com.newsvision.elasticsearch.controller;

import com.newsvision.elasticsearch.dto.PopularKeywordResponse;
import com.newsvision.elasticsearch.service.PopularKeywordService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/popular")
public class PopularKeywordController {
    @PostConstruct
    public void init() {
        System.out.println("✅ PopularKeywordController 로드됨");
    }
    private final PopularKeywordService popularKeywordService;

    @GetMapping("/{type}")
    public ResponseEntity<List<PopularKeywordResponse>> getPopularKeywords(@PathVariable String type) {
        log.info("🔥 /api/popular/{} 요청 받음", type); // ✅ 찍히는지 확인
        try {
            List<PopularKeywordResponse> result = popularKeywordService.getPopularKeywordsByType(type);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
