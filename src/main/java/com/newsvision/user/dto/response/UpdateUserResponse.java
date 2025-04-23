package com.newsvision.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserResponse {
    private final String image;
    private final String nickname;
    private final String email;
    private final String introduce;
}
