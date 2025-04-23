package com.newsvision.news.controller.response;

import com.newsvision.global.Utils.TimeUtil;
import com.newsvision.news.entity.News;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewsResponse {

    private Long id;
    private String title;
    private String content;
    private String image;
    private int likeCount;
    private int view;
    private String createdAt;
    private String authorNickname;
    private String authorBadgeIcon;
    private String category;
    private boolean liked;
    private boolean scraped;

    public static NewsResponse of(News news, int likeCount, boolean liked, boolean scraped) {
        return NewsResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .image(news.getImage())
                .likeCount(likeCount)
                .view(news.getView())
                .createdAt(TimeUtil.formatRelativeTime(news.getCreatedAt()))
                .authorNickname(news.getUser().getNickname())
                .authorBadgeIcon(news.getUser().getBadge() != null ? news.getUser().getBadge().getIcon() : null)
                .category(news.getCategory().getName())
                .liked(liked)
                .scraped(scraped)
                .build();
    }
}
