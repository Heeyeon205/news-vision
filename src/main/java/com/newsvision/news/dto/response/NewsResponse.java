package com.newsvision.news.dto.response;

import com.newsvision.global.Utils.TimeUtil;
import com.newsvision.news.entity.News;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewsResponse {
    private Long id;
    private String image;
    private String title;
    private String content;
    private String createdAt;
    private String category;

    private int likeCount;
    private int view;

    private boolean liked;
    private boolean scraped;
    private boolean followed;

    private Long userId;
    private String profileImage;
    private String nickname;
    private String icon;
    private String badgeTitle;

    public static NewsResponse of(News news, int likeCount, boolean liked, boolean scraped, boolean followed) {
        return NewsResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .image(news.getImage())
                .likeCount(likeCount)
                .view(news.getView())
                .createdAt(TimeUtil.formatRelativeTime(news.getCreatedAt()))
                .userId(news.getUser().getId())
                .profileImage(news.getUser().getImage())
                .nickname(news.getUser().getNickname())
                .icon(news.getUser().getBadge() != null ? news.getUser().getBadge().getIcon() : null)
                .category(news.getCategory().getName())
                .liked(liked)
                .scraped(scraped)
                .followed(followed)
                .badgeTitle(news.getUser().getBadge().getTitle())
                .build();
    }
}
