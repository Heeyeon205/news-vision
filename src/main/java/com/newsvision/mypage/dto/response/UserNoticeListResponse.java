package com.newsvision.mypage.dto.response;

import com.newsvision.global.Utils.TimeUtil;
import com.newsvision.notice.entity.Notice;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserNoticeListResponse {
    private Long id;
    private String type;
    private String title;
    private String url;
    private Boolean isRead;
    private String createAt;

    public static UserNoticeListResponse from(Notice notice) {
        return UserNoticeListResponse.builder()
                .id(notice.getId())
                .type(notice.getType().name())
                .title(notice.getTitle())
                .url(notice.getUrl())
                .isRead(notice.isRead())
                .createAt(TimeUtil.formatRelativeTime(notice.getCreatedAt()))
                .build();
    }
}
