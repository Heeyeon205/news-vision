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
    private final PopularKeywordService popularKeywordService;

    @GetMapping("/{type}")
    public ResponseEntity<List<PopularKeywordResponse>> getPopularKeywords(@PathVariable String type) {
            log.info("일단 여기는 들어옴 파퓰러 컨트롤러");
            log.info("getPopularKeywords type:{}", type);
            List<PopularKeywordResponse> result = popularKeywordService.getPopularKeywordsByType(type);
            log.info("여기가 리턴 직전 사이즈_{}", result.size());
            return ResponseEntity.ok(result);
}
}

