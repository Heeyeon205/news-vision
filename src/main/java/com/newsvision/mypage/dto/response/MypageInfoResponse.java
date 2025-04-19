package com.newsvision.mypage.dto.response;

import com.newsvision.user.entity.Badge;
import com.newsvision.user.entity.User;
import lombok.Getter;

@Getter
public class MypageInfoResponse {
    private final Long userId;
    private final String nickname;
    private final String image;
    private final String introduce;
    private final int followerCount;
    private final int followingCount;
    private String icon;
    private String title;

    public MypageInfoResponse(User user, int followerCount, int followingCount, Badge badge) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.introduce = user.getIntroduce();
        this.image = user.getImage();
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        if (badge != null) {
            this.icon = badge.getIcon();
            this.title = badge.getTitle();
        }
    }
}
