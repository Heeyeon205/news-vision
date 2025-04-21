package com.newsvision.news.service;

import com.newsvision.news.entity.News;
import com.newsvision.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GptNewsScheduler {
    private final NewsRepository newsRepository;
    private final GptSummaryService gptSummaryService;

    @Scheduled(cron = "0 0 0 * * *")
    public void summarizeTop10News() {
        log.info("뉴스 요약 실행");
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        List<News> top10News = newsRepository.findTopNewsByAdminOnly(threeDaysAgo, PageRequest.of(0, 10));

        for (News news : top10News) {
            log.info("요약 뉴스 Title: {}", news.getTitle());
            gptSummaryService.createSummary(news.getId(), news.getTitle(), news.getContent());
        }
        log.info("뉴스 요약 완료");
    }
}
