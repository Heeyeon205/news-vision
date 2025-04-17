package com.newsvision.board.controller.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponse {
    private Long id;
    private Long userId;
    private Long boardId;
    private boolean isReported;
    private String content;


    public CommentResponse(Long id, Long userId, Long boardId, boolean isReported, String content) {
        this.id = id;
        this.userId = userId;
        this.boardId = boardId;
        this.isReported = isReported;
        this.content = content;

    }
}
