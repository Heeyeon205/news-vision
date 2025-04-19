package com.newsvision.user.dto.request;

import com.newsvision.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserRequest {
    private String image;
    private String nickname;
    private String introduce;

    public User toEntity(){
        return User.builder()
                .image(this.image)
                .nickname(this.nickname)
                .introduce(this.introduce)
                .build();
    }
}
