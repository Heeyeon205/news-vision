package com.newsvision.elasticsearch.repository;

import com.newsvision.elasticsearch.document.NewsDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface NewsSearchRepository extends ElasticsearchRepository<NewsDocument, Long> {
    // 커스텀 쿼리는 필요 시 여기 추가
    @Query("""
        {
          "bool": {
            "should": [
              { "match": { "title": "?0" }},
              { "match": { "content": "?0" }}
            ]
          }
        }
    """)
    List<NewsDocument> searchByTitleOrContent(String keyword);
}
