package com.newsvision.poll.controller.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PollResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private String authorNickname; // 투표 작성자 닉네임
    private List<PollOptionResponse> pollOptions;
}
