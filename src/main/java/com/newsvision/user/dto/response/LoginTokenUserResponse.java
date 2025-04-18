package com.newsvision.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class LoginTokenUserResponse {
    private String accessToken;
    private String refreshToken;
}
