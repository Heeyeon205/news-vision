package com.newsvision.board.controller.response;

import com.newsvision.board.entity.Board;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardResponse {
    private String title;
    private String content;
    private Long categoryId;
    private LocalDateTime createdAt;
    private Long userId;
    private String image;
    private Long view;
    private Long newsId;
    private Boolean isReported;
    private Long likeCount;
    private Long commentCount;

    public BoardResponse(String title, String content, Long categoryId, LocalDateTime createdAt, Long userId, String image, Long view, Long newsId, Boolean isReported, Long likeCount, Long commentCount) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
        this.userId = userId;
        this.image = image;
        this.view = view;
        this.newsId = newsId;
        this.isReported = isReported;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
}
