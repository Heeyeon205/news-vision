package com.newsvision.news.dto.response;

import com.newsvision.global.Utils.TimeUtil;
import com.newsvision.news.entity.News;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewsSummaryResponse {
    private Long id;
    private String title;
    private String image;
    private String category;
    private String nickname;
    private String createdAt;

    public static NewsSummaryResponse from(News news) {
        return NewsSummaryResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .image(news.getImage())
                .category(news.getCategory().getName())
                .nickname(news.getUser().getNickname())
                .createdAt(TimeUtil.formatRelativeTime(news.getCreatedAt()))
                .build();
    }
}
