package com.newsvision.admin.controller;

import com.newsvision.admin.service.NewsListService;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.news.controller.response.NewsResponse;
import com.newsvision.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/admin/news") // 관리자용 뉴스 경로
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NewsListController {

    private final NewsListService newsListService; // 목록 조회용 서비스
    private final NewsService newsService;       // 개별 뉴스 처리용 서비스
    private static final Logger log = LoggerFactory.getLogger(NewsListController.class); // 클래스 이름 일치

    /**
     * 관리자용 뉴스 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<NewsResponse>> getNewsList() {
        log.info("[GET /admin/news] Fetching all news for admin.");
        try {
            List<NewsResponse> newsList = newsListService.getAllNews();
            return ResponseEntity.ok(newsList);
        } catch (Exception e) {
            log.error("[GET /admin/news] Error fetching news list.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of()); // 오류 시 빈 리스트 또는 에러 객체 반환 고려
        }
    }
    @DeleteMapping("/{newsId}")
    public ResponseEntity<?> deleteMemberAjax(@PathVariable Long newsId,
                                              @RequestParam Long userId) {
        log.info("Deleting member with userId: {}", userId);
        log.info("Deleting newsId: {}", newsId);
        try {
            newsService.deleteNews(userId, newsId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }



}