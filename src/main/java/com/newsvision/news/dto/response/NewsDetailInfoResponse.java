package com.newsvision.news.dto.response;

import com.newsvision.category.Categories;
import com.newsvision.category.CategoryResponse;
import com.newsvision.category.CategoryService;
import com.newsvision.global.Utils.TimeUtil;
import com.newsvision.news.entity.News;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsDetailInfoResponse {
    private Long refId;
    private String refTitle;
    private String refPubdate;
    private String refLink;

    private Long id;
    private String image;
    private String title;
    private String content;
    private Long categoryId;

    private List<CategoryResponse> list;

    public static NewsDetailInfoResponse from(News news, List<CategoryResponse> categories) {
        return new NewsDetailInfoResponse(
                news.getNaverNews().getId(),
                news.getNaverNews().getTitle(),
                TimeUtil.formatRelativeTime(news.getNaverNews().getPublishedAt()),
                news.getNaverNews().getLink(),
                news.getId(),
                news.getImage(),
                news.getTitle(),
                news.getContent(),
                news.getCategory().getId(),
                categories
        );
    }
}
