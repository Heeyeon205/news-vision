package com.newsvision.board.controller.response;


import com.newsvision.board.entity.Comment;
import com.newsvision.global.Utils.TimeUtil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {
    private Long id;
    private Long userId;
    private Long boardId;
    private String image;
    private String icon;
    private String nickname;
    private boolean isReported;
    private String content;
    private String createdAt;


    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.userId = comment.getUser().getId();
        this.boardId = comment.getBoard().getId();
        this.image = comment.getUser().getImage();
        this.icon = comment.getUser().getBadge().getIcon();
        this.nickname = comment.getUser().getNickname();
        this.isReported = comment.getIsReported();
        this.content = comment.getContent();
        this.createdAt = TimeUtil.formatRelativeTime(comment.getCreateAt()); // getCreateAt() -> getCreatedAt()

    }
}
