package com.newsvision.board.dto.response;


import com.newsvision.board.entity.Comment;
import com.newsvision.global.Utils.TimeUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponse {
    private Long id;
    private String content;
    private boolean isReported;
    private String createdAt;

    private Long boardId;

    private Long userId;
    private String image;
    private String icon;
    private String nickname;


    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.userId = comment.getUser().getId();
        this.boardId = comment.getBoard().getId();
        this.image = comment.getUser().getImage();
        if(comment.getUser().getRole().name().equals("ROLE_ADMIN") && comment.getUser().getRole().name().equals("ROLE_CREATOR")){
            this.icon = comment.getUser().getBadge().getIcon();
        }
        this.nickname = comment.getUser().getNickname();
        this.isReported = comment.getIsReported();
        this.content = comment.getContent();
        this.createdAt = TimeUtil.formatRelativeTime(comment.getCreateAt()); // getCreateAt() -> getCreatedAt()

    }
}
