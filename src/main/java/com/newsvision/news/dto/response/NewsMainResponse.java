package com.newsvision.news.dto.response;

import com.newsvision.global.Utils.TimeUtil;
import com.newsvision.news.entity.News;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class NewsMainResponse {
    private Long id;
    private String title;
    private String image;
    private String category;
    private String nickname;
    private String createdAt;

    public static NewsMainResponse from(News news) {
        return NewsMainResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .image(news.getImage())
                .category(news.getCategory().getName())
                .nickname(news.getUser().getNickname())
                .createdAt(TimeUtil.formatRelativeTime(news.getCreatedAt()))
                .build();
    }

    public static List<NewsMainResponse> fromList(List<News> newsList) {
        return newsList.stream()
                .map(NewsMainResponse::from)
                .toList();
    }
}
