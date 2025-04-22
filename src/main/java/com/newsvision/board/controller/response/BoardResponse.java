package com.newsvision.board.controller.response;

import com.newsvision.board.entity.Board;
import com.newsvision.category.entity.Categories;
import com.newsvision.global.Utils.TimeUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BoardResponse {
    private Long boardId;
    private String nickname;
    private String userImage;
    private String icon;
    private String image;
    private String content;
    private Long categoryId;
    private String createAt;
    private Long view;
    private Long newsId;
    private Boolean isReported;
    private int likeCount;
    private int commentCount;

    public BoardResponse(Board board, int likeCount, int commentCount) {
        this.boardId = board.getId();
        this.nickname = board.getUser().getNickname();
        this.userImage = board.getUser().getImage();
        this.icon = board.getUser().getBadge() != null ? board.getUser().getBadge().getIcon() : null;
        this.image = board.getImage();
        this.content = board.getContent();
        this.categoryId = board.getCategory() != null ? board.getCategory().getId() : null;
        this.createAt = TimeUtil.formatRelativeTime(board.getCreateAt()); // getCreateAt() -> getCreatedAt()
        this.view = board.getView();
        this.newsId = board.getNewsId();
        this.isReported = board.getIsReported();
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
}
