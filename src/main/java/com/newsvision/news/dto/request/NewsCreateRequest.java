package com.newsvision.news.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsCreateRequest {
    private String title;
    private String content;
    private String image;
    private Long categoryId;
    private Long naverNewsId;
}