package com.newsvision.admin.controller.response;



import com.newsvision.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserListResponse {
    private Long id;
    private String username;
    private String nickname;
    private String role;
    private Boolean isPaid;
    private LocalDateTime created_at;
    private String introduce;
    private String image;
    private String provider_id;
    public UserListResponse(User userListEntity) {
        this.id = userListEntity.getId();
        this.username = userListEntity.getUsername();
        this.nickname = userListEntity.getNickname();
        this.role = userListEntity.getRole().name();
        this.isPaid = userListEntity.getIsPaid();
        this.created_at = userListEntity.getCreateAt();
        this.introduce = userListEntity.getIntroduce();
        this.image = userListEntity.getImage();
        this.provider_id = userListEntity.getProviderId();
    }
}