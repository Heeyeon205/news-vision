package com.newsvision.elasticsearch.document;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;

@Document(indexName = "boards")
@Getter
@Builder
public class BoardDocument {
    @Id
    private Long id;
    private String content;
    private String categoryName;
    private String username;
    private String image;
    private LocalDateTime createdAt;
}
