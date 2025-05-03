package com.newsvision.mypage.dto.response;

import com.newsvision.user.entity.Badge;
import com.newsvision.user.entity.User;
import lombok.Getter;

@Getter
public class OtherUserInfoResponse {
    private final Long userId;
    private final String nickname;
    private final String image;
    private final String introduce;
    private final int followerCount;
    private final int followingCount;
    private String icon;
    private String title;
    private final boolean followed;

    public OtherUserInfoResponse(User user, int followerCount, int followingCount, Badge badge, boolean followed) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.introduce = user.getIntroduce();
        this.image = user.getImage();
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.followed = followed;
        if (badge != null) {
            this.icon = badge.getIcon();
            this.title = badge.getTitle();
        }
    }
}
