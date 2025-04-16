package com.newsvision.news.controller.response;

import com.newsvision.news.entity.News;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NewsSummaryResponse {

    private Long id;
    private String title;
    private String image;
    private String category;
    private String author;
    private LocalDateTime createdAt;

    public static NewsSummaryResponse from(News news) {
        return NewsSummaryResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .image(news.getImage())
                .category(news.getCategory().getName())
                .author(news.getUser().getNickname())
                .createdAt(news.getCreatedAt())
                .build();
    }
}
