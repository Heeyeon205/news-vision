package com.newsvision.poll.dto.response;

import com.newsvision.global.Utils.TimeUtil;
import com.newsvision.poll.entity.Poll;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PollListResponse {
    private final Long id;
    private final String title;
    private String createdAt;
    private String expiredAt;
    private String nickname;

    public PollListResponse(Poll poll) {
        this.id = poll.getId();
        this.title = poll.getTitle();
        this.createdAt = TimeUtil.formatRelativeTime(poll.getCreatedAt());
        this.expiredAt = TimeUtil.dDayCaculate(poll.getExpiredAt());
        this.nickname = poll.getUser().getNickname();
    }
}
