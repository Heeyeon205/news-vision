package com.newsvision.news.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsUpdateRequest {
    private Long newsId;
    private String title;
    private String content;
    private String image;
    private Long categoryId;
}
