package com.newsvision.user.dto.request;

import com.newsvision.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserRequest {
    private Long id;
    private String image;
    private String nickname;
    private String introduce;

    public User toEntity(){
        return User.builder()
                .id(this.id)
                .image(this.image)
                .nickname(this.nickname)
                .introduce(this.introduce)
                .build();
    }
}
