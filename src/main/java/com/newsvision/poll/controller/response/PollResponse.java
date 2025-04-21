package com.newsvision.poll.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter

public class PollResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private String authorNickname; // 투표 작성자 닉네임
    private List<PollOptionResponse> pollOptions;
}
