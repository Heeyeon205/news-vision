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

    // 메인등의 화면에서 카드형식으로 news정보 담아 보여주는 response
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
