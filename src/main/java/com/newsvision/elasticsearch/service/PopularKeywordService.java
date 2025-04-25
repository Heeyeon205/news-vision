package com.newsvision.elasticsearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsvision.elasticsearch.dto.PopularKeywordResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PopularKeywordService {
    private final ElasticsearchClient esClient;

    public List<PopularKeywordResponse> getPopularKeywordsByType(String type) throws Exception {
        try {
            String json = """
            {
              "query": {
                "bool": {
                  "must": [
                    {
                      "range": {
                        "@timestamp": {
                          "gte": "now-3d/d"
                        }
                      }
                    },
                    {
                      "term": {
                        "type.keyword": {
                          "value": "%s"
                        }
                      }
                    }
                  ]
                }
              },
              "aggs": {
                "top_keywords": {
                  "terms": {
                    "field": "keyword.keyword",
                    "size": 10
                  }
                }
              }
            }
            """.formatted(type);

            SearchRequest request = SearchRequest.of(b -> b
                    .index("search-logs")
                    .withJson(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)))
            );

            SearchResponse<Void> response = esClient.search(request, Void.class);

            if (response.aggregations() == null) {
                log.error("🔥 aggregation 결과가 null임");
                throw new IllegalStateException("Aggregation 결과가 null임");
            }


            return response.aggregations()
                    .get("top_keywords")
                    .sterms()
                    .buckets()
                    .array()
                    .stream()
                    .map(bucket -> new PopularKeywordResponse(bucket.key().stringValue(), bucket.docCount()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("🔥 Elasticsearch 쿼리 중 예외 발생", e); // 여기에 찍힌다!
            throw e;
        }
    }
}
