package com.newsvision.mypage.response;

import com.newsvision.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FollowResponse {
    private Long id;
    private String image;
    private String nickname;
    private String badge;
    private String introduction;

    public static FollowResponse from(User user) {
        return FollowResponse.builder()
                .id(user.getId())
                .image(user.getImage())
                .nickname(user.getNickname())
                .badge(user.getBadge() != null ? user.getBadge().getIcon() : "")
                .introduction(user.getIntroduce() != null ? user.getIntroduce() : "")
                .build();
    }
}
