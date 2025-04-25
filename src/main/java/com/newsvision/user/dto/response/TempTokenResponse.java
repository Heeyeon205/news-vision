package com.newsvision.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TempTokenResponse {
    private String nickname;
    private String tempToken;
}
