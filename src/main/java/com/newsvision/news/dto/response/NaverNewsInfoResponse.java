package com.newsvision.news.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaverNewsInfoResponse {
    private Long id;
    private String title;
    private String originallink;
    private String link;
    private String description;
    private String pubDate;
    }
