package com.newsvision.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
public class NoticeUserResponse {
    private Long id;
    private String title;
    private String url;
    private boolean isRead;
    private String createdAt;

    private Long userId;
    private String image;
    private String nickname;

}
