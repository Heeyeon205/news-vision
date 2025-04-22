package com.newsvision.elasticsearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.newsvision.elasticsearch.document.NewsDocument;
import com.newsvision.elasticsearch.repository.NewsSearchRepository;
import com.newsvision.news.controller.response.NewsSummaryResponse;
import com.newsvision.news.entity.News;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.elasticsearch.core.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class NewsSearchService {

    private final NewsSearchRepository newsSearchRepository;
    private final ElasticsearchClient elasticsearchClient;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    // 🟢 뉴스 저장
    public void saveNews(News news) {


        NewsDocument doc = NewsDocument.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .categoryName(news.getCategory().getName())
                .username(news.getUser().getUsername())
                .image(news.getImage())
                .createdAt(news.getCreatedAt().format(formatter)) // ⭐ 포맷 처리
                .build();

        newsSearchRepository.save(doc);
        log.info("✅ 뉴스 검색 인덱스 저장 완료");
    }


    // 🔴 뉴스 삭제
    public void deleteNews(Long newsId) {
        newsSearchRepository.deleteById(newsId);
    }

    // 🔍 뉴스 검색 (title + content)
    public List<NewsSummaryResponse> searchNews(String keyword) throws Exception {
        String analyzerSuffix = getAnalyzerSuffix(keyword);

        SearchResponse<NewsDocument> response = elasticsearchClient.search(s -> s
                        .index("news")
                        .query(q -> q
                                .bool(b -> b
                                        .should(QueryBuilders.match(m -> m
                                                .field("title." + analyzerSuffix)
                                                .query(keyword)
                                        ))
                                        .should(QueryBuilders.match(m -> m
                                                .field("content." + analyzerSuffix)
                                                .query(keyword)
                                        ))
                                )
                        ),
                NewsDocument.class
        );

        return response.hits().hits().stream()
                .map(Hit::source)
                .map(doc -> NewsSummaryResponse.builder()
                        .id(doc.getId())
                        .title(doc.getTitle())
                        .image(doc.getImage())
                        .category(doc.getCategoryName())
                        .author(doc.getUsername())
                        .createdAt(LocalDateTime.parse(doc.getCreatedAt(), formatter))
                        .build())
                .toList();
    }

    private String getAnalyzerSuffix(String input) {
        boolean hasKor = input.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*");
        boolean hasEng = input.matches(".*[a-zA-Z]+.*");

        if (hasKor && hasEng) return "mixed";
        if (hasKor) return "kor";
        return "eng";
    }
}
