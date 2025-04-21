package com.newsvision.news.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaverNewsInfoResponse {
    private String title;
    private String originallink;
    private String link;
    private String description;
    private String pubDate;
}
