package com.newsvision.board.dto.response;

import com.newsvision.board.entity.Board;
import com.newsvision.global.Utils.TimeUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardResponse {
    private Long boardId;
    private String nickname;
    private String userImage; //유저 이미지
    private String icon;
    private String image; // 보드 이미지
    private String content;
    private Long categoryId;
    private String createdAt;
    private int view;
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
        this.createdAt = TimeUtil.formatRelativeTime(board.getCreatedAt()); // getCreateAt() -> getCreatedAt()
        this.view = board.getView();
        this.newsId = board.getNewsId();
        this.isReported = board.getIsReported();
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
}
