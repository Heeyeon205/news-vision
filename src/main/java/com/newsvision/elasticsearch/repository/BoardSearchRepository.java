package com.newsvision.elasticsearch.repository;

import com.newsvision.elasticsearch.document.BoardDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BoardSearchRepository extends ElasticsearchRepository<BoardDocument, Long> {
    @Query("""
        {
          "match": {
            "content": "?0"
          }
        }
    """)
    List<BoardDocument> searchByContent(String keyword);
}
