package com.newsvision.admin.controller.response;



import com.newsvision.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.management.relation.Role;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserListResponse {


    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String role;
    private Boolean isPaid;
    private LocalDateTime create_at;
    private String introduce;
    private String image;
    private String provider_id;
    private User.Provider provider;
    public UserListResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.role = user.getRole().name();
        this.isPaid = user.getIsPaid();
        this.create_at = user.getCreateAt();
        this.introduce = user.getIntroduce();
        this.image = user.getImage();
        this.provider_id = user.getProviderId();
        this.provider=user.getProvider();
    }

}