package com.newsvision.board.controller.response;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardDetailResponse {
    private Long id;
    private String content;
    private Long categoryId;
    private LocalDateTime createdAt;
    private String relativeCreatedAt;
    private Long userId;
    private String image;
    private int view;
    private Long newsId;
    private Boolean isReported;
    private int likeCount;
    private int commentCount;

    public BoardDetailResponse(Long id, String content, Long categoryId, LocalDateTime createdAt,String relativeCreatedAt, Long userId, String image, int view, Long newsId, Boolean isReported, int likeCount, int commentCount) {
        this.id = id;
        this.content = content;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
        this.relativeCreatedAt = relativeCreatedAt;
        this.userId = userId;
        this.image = image;
        this.view = view;
        this.newsId = newsId;
        this.isReported = isReported;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
}