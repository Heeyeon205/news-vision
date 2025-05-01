package com.newsvision.poll.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class PollOptionResponse {
    private final Long id;
    private final String content;
    private final int count;
}