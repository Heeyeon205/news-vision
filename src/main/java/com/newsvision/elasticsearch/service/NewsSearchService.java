package com.newsvision.elasticsearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.newsvision.elasticsearch.document.NewsDocument;
import com.newsvision.elasticsearch.repository.NewsSearchRepository;
import com.newsvision.global.Utils.JasoUtils;
import com.newsvision.news.dto.response.NewsSummaryResponse;
import com.newsvision.news.entity.News;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public void saveNews(News news) {

        NewsDocument doc = NewsDocument.builder()
                .id(news.getId())
                .title(news.getTitle())
                .titleJaso(JasoUtils.splitJaso(news.getTitle())) // 추가
                .titleChosung(JasoUtils.extractChosung(news.getTitle()))
                .content(news.getContent())
                .categoryName(news.getCategory().getName())
                .username(news.getUser().getNickname())
                .image(news.getImage())
                .createdAt(news.getCreatedAt().format(formatter))
                .build();

        newsSearchRepository.save(doc);
        log.info("뉴스 검색 인덱스 저장 완료");
    }

    public void deleteNews(Long newsId) {
        newsSearchRepository.deleteById(newsId);
    }

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
                        .nickname(doc.getUsername())
                        .createdAt(String.valueOf(LocalDateTime.parse(doc.getCreatedAt(), formatter)))
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
    public List<String> autocompleteTitle(String keyword) throws Exception {
        String jasoQuery = JasoUtils.splitJaso(keyword);
        String chosungQuery = JasoUtils.extractChosung(keyword);

        SearchResponse<NewsDocument> response = elasticsearchClient.search(s -> s
                        .index("news")
                        .size(20)
                        .query(q -> q
                                .bool(b -> b
                                        .should(m -> m.match(mm -> mm
                                                .field("titleJaso")
                                                .query(jasoQuery)))
                                        .should(m -> m.match(mm -> mm
                                                .field("titleChosung")
                                                .query(chosungQuery)))
                                )
                        ),
                NewsDocument.class
        );

        return response.hits().hits().stream()
                .map(hit -> hit.source().getTitle())
                .flatMap(title -> {
                    log.info("title: {}, jaso: {}, chosung: {}", title, JasoUtils.splitJaso(title), JasoUtils.extractChosung(title));
                    return List.of(title.split(" ")).stream()
                            .filter(word ->
                                    JasoUtils.splitJaso(word).contains(jasoQuery) ||
                                            JasoUtils.extractChosung(word).contains(chosungQuery)
                            );
                })
                .distinct()
                .limit(10)
                .toList();
    }
}
