package com.newsvision.news.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaverNewsSaveRequest {
    private String title;
    private String description;
    private String link;
    private String pubDate;
}
