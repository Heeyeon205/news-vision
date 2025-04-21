package com.newsvision.news.controller.request;

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
    private String image; // 이미지 URL
    private Long categoryId;
    private Long naverNewsId;
}