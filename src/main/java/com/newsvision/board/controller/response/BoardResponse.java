package com.newsvision.board.controller.response;

import com.newsvision.board.entity.Board;
import com.newsvision.category.entity.Categories;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class BoardResponse {
    private String content;
    private Long categoryId;
    private LocalDateTime createdAt;
    private String relativeCreatedAt;
    private Long userId;
    private String image;
    private Long view;
    private Long newsId;
    private Boolean isReported;
    private Long likeCount;
    private Long commentCount;

    public BoardResponse(String content, Long categoryId, LocalDateTime createdAt,String relativeCreatedAt, Long userId, String image, Long view, Long newsId, Boolean isReported, Long likeCount, Long commentCount) {
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
