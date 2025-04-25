package com.newsvision.user.dto.response;

import com.newsvision.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserResponse {
    private final String image;
    private final String nickname;
    private final String email;
    private final String introduce;

    public static UpdateUserResponse from(User user) {
        return new UpdateUserResponse(
                user.getImage(),
                user.getNickname(),
                user.getEmail(),
                user.getIntroduce()
        );
    }
}
