package com.newsvision.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PopularKeywordResponse {
    private String keyword;
    private long count;
}
