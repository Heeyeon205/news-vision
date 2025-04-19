package com.newsvision.board.controller.response;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {
    private Long id;
    private Long userId;
    private Long boardId;
    private boolean isReported;
    private String content;
    private LocalDateTime createdAt;
    private String relativeCreatedAt; // 보이는 시간 몇시간전


    public CommentResponse(Long id, Long userId, Long boardId, boolean isReported, String content, LocalDateTime createdAt, String relativeCreatedAt) {
        this.id = id;
        this.userId = userId;
        this.boardId = boardId;
        this.isReported = isReported;
        this.content = content;
        this.createdAt = createdAt;
        this.relativeCreatedAt = relativeCreatedAt;

    }
}
