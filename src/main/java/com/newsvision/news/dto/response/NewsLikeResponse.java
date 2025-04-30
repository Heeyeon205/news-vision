package com.newsvision.news.dto.response;

import com.newsvision.global.Utils.TimeUtil;
import com.newsvision.news.entity.News;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewsLikeResponse {
    private int likeCount;
    private boolean isLike;
}
