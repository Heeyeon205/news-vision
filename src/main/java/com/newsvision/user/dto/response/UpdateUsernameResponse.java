package com.newsvision.user.dto.response;

import com.newsvision.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class UpdateUsernameResponse {
    private final String username;
}
