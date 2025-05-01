package com.newsvision.poll.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Builder
public class PollResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String createdAt;
    private final String expiredAt;

    private final Long userId;
    private final String image;
    private final String nickname;
    private final String icon;
    private final String badgeTitle;

    private final boolean isVote;
    private final boolean followed;

    private final List<PollOptionResponse> pollOptions;
}
