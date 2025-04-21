package com.newsvision.user.dto.response;

import com.newsvision.user.entity.Follow;
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

    public static FollowResponse from(User user) {
        return FollowResponse.builder()
                .id(user.getId())
                .image(user.getImage())
                .badge(user.getBadge().getIcon())
                .nickname(user.getNickname())
                .build();
    }
}
