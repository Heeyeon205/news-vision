package com.newsvision.user.dto.request;

import com.newsvision.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinUserRequest {
    private String username;
    private String password;
    private String email;

    public User toEntity(){
        return User.builder()
                .username(this.username)
                .password(this.password)
                .email(this.email)
                .build();
    }
}
