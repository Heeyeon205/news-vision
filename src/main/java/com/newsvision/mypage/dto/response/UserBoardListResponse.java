package com.newsvision.mypage.dto.response;

import com.newsvision.board.entity.Board;
import com.newsvision.global.Utils.TimeUtil;
import com.newsvision.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserBoardListResponse {
    private final Long boardId;
    private final String nickname;
    private final String icon;
    private final String image;
    private final String content;
    private final String createAt;
    private final int likeCount;
    private final int commentCount;

    public static UserBoardListResponse from(Board board, int likeCount, int commentCount) {
        User user = board.getUser();
        return UserBoardListResponse.builder()
                .boardId(board.getId())
                .nickname(user.getNickname())
                .icon(user.getBadge() != null ? user.getBadge().getIcon() : null)
                .content(board.getContent())
                .image(board.getImage())
                .createAt(TimeUtil.formatRelativeTime(board.getCreateAt()))
                .likeCount(likeCount)
                .commentCount(commentCount)
                .build();
    }
}
