package com.newsvision.news.dto.response;

import com.newsvision.poll.controller.response.PollListResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class NewsMainDataResponse {
    private List<NewsMainResponse> newsList;
    private List<PollListResponse> pollList;
}
