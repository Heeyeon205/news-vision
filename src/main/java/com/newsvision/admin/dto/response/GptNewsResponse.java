package com.newsvision.admin.dto.response;

import com.newsvision.global.Utils.TimeUtil;
import com.newsvision.news.entity.GptNews;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GptNewsResponse {

    private Long id;
    private Long newsId;
    private String title;
    private String summary;
    private String createdAt;

    // Constructs response from GptNews entity
    public static GptNewsResponse of(GptNews gptNews) {
        return GptNewsResponse.builder()
                .id(gptNews.getId())
                .newsId(gptNews.getNewsId())
                .title(gptNews.getTitle())
                .summary(gptNews.getSummary())
                .createdAt(TimeUtil.formatRelativeTime(gptNews.getCreatedAt()))
                .build();
    }
}