package com.newsvision.poll.controller.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CreatePollRequest {
    private String title;
    private String content;
    private LocalDateTime expiredAt;
    private List<String> options; // 투표 선택지 목록
}
