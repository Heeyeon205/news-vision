package com.newsvision.notice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NoticeEventResponse {
    private Long senderId;
    private String type;
    private String title;
    private String url;
    private boolean read;
}
