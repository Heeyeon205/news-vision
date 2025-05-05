package com.newsvision.user.dto.response;

import com.newsvision.user.entity.User;
import lombok.Getter;

@Getter
public class UpdateResultResponse {
    private final Long userId;
    private final String nickname;
    private final String image;
    private final String role;

    public UpdateResultResponse(User user) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.image = user.getImage();
        this.role = user.getRole().name();
    }
}