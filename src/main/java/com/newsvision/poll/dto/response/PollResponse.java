package com.newsvision.poll.dto.response;

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
    private String nickname;
    private boolean isVote;
    private List<PollOptionResponse> pollOptions;
}
