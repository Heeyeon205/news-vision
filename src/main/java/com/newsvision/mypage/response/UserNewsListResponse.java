package com.newsvision.mypage.response;

import com.newsvision.global.Utils.TimeUtil;
import com.newsvision.news.entity.News;
import com.newsvision.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserNewsListResponse {
    private Long newsId;
    private String image;
    private String title;
    private String createAt;
    private String nickname;
    private int likeCount;

    public static UserNewsListResponse from(News news, int likeCount) {
        User user = news.getUser();
        return UserNewsListResponse.builder()
                .newsId(news.getId())
                .image(news.getImage())
                .title(news.getTitle())
                .createAt(TimeUtil.formatRelativeTime(news.getCreatedAt()))
                .nickname(user.getNickname())
                .likeCount(likeCount)
                .build();
    }
}
