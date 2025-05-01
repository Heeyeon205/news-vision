package com.newsvision.poll.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PollVoteResponse {
    private int voteCount;
    private boolean isPoll;
}
