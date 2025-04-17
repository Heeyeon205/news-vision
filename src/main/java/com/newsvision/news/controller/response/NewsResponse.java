package com.newsvision.news.controller.response;

import com.newsvision.news.entity.News;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NewsResponse {

    private Long id;
    private String title;
    private String content;
    private String image;
    private int likeCount;
    private int view;
    private LocalDateTime createdAt;
    private String author;
    private String category;
    private boolean liked;
    private boolean scraped;

    //상세보기 등의 화면에서 news 전체 정보 가져와 담아주는 response
    public static NewsResponse of(News news, int likeCount, boolean liked, boolean scraped) {
        return NewsResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .image(news.getImage())
                .likeCount(likeCount)
                .view(news.getView())
                .createdAt(news.getCreatedAt())
                .author(news.getUser().getNickname())
                .category(news.getCategory().getName())
                .liked(liked)
                .scraped(scraped)
                .build();
    }
}
