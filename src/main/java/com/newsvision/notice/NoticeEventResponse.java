package com.newsvision.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NoticeEventResponse {
    private String type;
    private String title;
    private String url;
    private boolean read;
}
