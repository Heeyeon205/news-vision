package com.newsvision.elasticsearch.service;

import com.newsvision.elasticsearch.document.NewsDocument;
import com.newsvision.elasticsearch.repository.NewsSearchRepository;
import com.newsvision.news.controller.response.NewsSummaryResponse;
import com.newsvision.news.entity.News;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsSearchService {

    private final NewsSearchRepository newsSearchRepository;

    // 🟢 뉴스 저장
    public void saveNews(News news) {
        NewsDocument doc = NewsDocument.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .categoryName(news.getCategory().getName())
                .username(news.getUser().getUsername())
                .image(news.getImage())
                .createdAt(news.getCreatedAt())
                .build();

        newsSearchRepository.save(doc);
    }

    // 🔴 뉴스 삭제
    public void deleteNews(Long newsId) {
        newsSearchRepository.deleteById(newsId);
    }

    // 🔍 뉴스 검색 (title + content)
    public List<NewsSummaryResponse> searchNews(String keyword) {
        return newsSearchRepository.searchByTitleOrContent(keyword)
                .stream()
                .map(doc -> NewsSummaryResponse.builder()
                        .id(doc.getId())
                        .title(doc.getTitle())
                        .image(doc.getImage())
                        .category(doc.getCategoryName())
                        .author(doc.getUsername())
                        .createdAt(doc.getCreatedAt())
                        .build()
                ).toList();
    }
}
