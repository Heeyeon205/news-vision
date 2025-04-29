package com.newsvision.board.controller.response;

import com.newsvision.board.entity.Board;
import com.newsvision.category.CategoryResponse;
import com.newsvision.global.Utils.TimeUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BoardUpdateResponse {
    private Long id;
    private String image; // 보드 이미지
    private String content;
    private Long categoryId;
    private String createdAt;
    private int view;
    private int likeCount;
    private int commentCount;
    private Boolean isReported;

    private Long userId;
    private String userImage; // 유저 이미지
    private String nickname;
    private String icon;

    private Long newsId;

    private List<CommentResponse> comments;
    private List<CategoryResponse> categories;

    public BoardUpdateResponse(Board board, int likeCount, int commentCount, List<CommentResponse> comments, List<CategoryResponse> categories) {
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
        this.commentCount = commentCount;
        this.comments = comments;
        this.categories = categories;
    }
}
