package com.newsvision.admin.controller.response;

import com.newsvision.global.Utils.TimeUtil;
import com.newsvision.news.entity.NaverNews;
import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class NaverNewsResponse {

    private Long id;
    private String title;
    private String description;
    private String link;
    private String publishedAt;

    // Constructs response from NaverNews entity
    public static NaverNewsResponse of(NaverNews naverNews) {
        return NaverNewsResponse.builder()
                .id(naverNews.getId())
                .title(naverNews.getTitle())
                .description(naverNews.getDescription())
                .link(naverNews.getLink())
                .publishedAt(TimeUtil.formatRelativeTime(naverNews.getPublishedAt()))
                .build();
    }
}