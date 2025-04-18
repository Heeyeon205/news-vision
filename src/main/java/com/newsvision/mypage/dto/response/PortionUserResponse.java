package com.newsvision.mypage.dto.response;

import com.newsvision.user.entity.User;
import lombok.Getter;

@Getter
public class PortionUserResponse {
    private Long userId;
    private String nickname;
    private String image;
    // private int followerCount;
    // private int followingCount;

    public PortionUserResponse(User user) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.image = user.getImage();
    }
}
