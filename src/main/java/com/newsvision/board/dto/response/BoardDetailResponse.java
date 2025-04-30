package com.newsvision.board.dto.response;


import com.newsvision.board.entity.Board;
import com.newsvision.global.Utils.TimeUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardDetailResponse {
    private Long userId;
    private String nickname;
    private String userImage; // 유저 이미지
    private String icon;

    private Long id;
    private String image; // 보드 이미지
    private String content;
    private Long categoryId;
    private String createdAt;
    private int view;

    private Boolean isReported;
    private boolean isLike;
    private int likeCount;
    private int commentCount;

    private Long newsId;

    private List<CommentResponse> comments;

    public BoardDetailResponse(Board board, int likeCount, int commentCount, List<CommentResponse> comments, boolean isLike) {
        this.id = board.getId();
        this.nickname = board.getUser().getNickname();
        this.userImage = board.getUser().getImage();
        this.icon = board.getUser().getBadge() != null ? board.getUser().getBadge().getIcon() : null;
        this.content = board.getContent();
        this.categoryId = board.getCategory() != null ? board.getCategory().getId() : null;
        this.createdAt = TimeUtil.formatRelativeTime(board.getCreateAt());
        this.userId = board.getUser().getId();
        this.image = board.getImage();
        this.view = board.getView();
        this.newsId = board.getNewsId();
        this.isReported = board.getIsReported();
        this.likeCount = likeCount;
        this.isLike = isLike;
        this.commentCount = commentCount;
        this.comments = comments;
    }
}