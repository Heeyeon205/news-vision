package com.newsvision.poll.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UpdatePollRequest {
    private String title;
    private String content;
    private LocalDateTime expiredAt; // 만료일
    private List<String> options; // 투표 선택지 목록
}