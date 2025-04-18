package com.newsvision.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserFollowCountResponse {
    private final int followerCount;
    private final int followingCount;
}
