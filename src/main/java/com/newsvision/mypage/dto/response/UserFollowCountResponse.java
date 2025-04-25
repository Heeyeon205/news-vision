package com.newsvision.mypage.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserFollowCountResponse {
    private final int followerCount;
    private final int followingCount;
}
