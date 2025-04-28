package com.newsvision.user.dto.response;

import com.newsvision.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class LoginTokenUserResponse {
    private String accessToken;
    private String refreshToken;

    private Long userId;
    private String nickname;
    private String image;
}
