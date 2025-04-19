package com.newsvision.mypage.service;

import com.newsvision.mypage.dto.response.MypageInfoResponse;
import com.newsvision.user.entity.Badge;
import com.newsvision.user.entity.User;
import com.newsvision.user.service.BadgeService;
import com.newsvision.user.service.FollowService;
import com.newsvision.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class MypageService {
    private final UserService userService;
    private final FollowService followService;
    private final BadgeService badgeService;

    public MypageInfoResponse getPortionUser(Long userId) {
        User user = userService.findByUserId(userId);
        int followerCount = followService.getCountFollower(user);
        int followingCount = followService.getCountFollowing(user);
        Badge badge = badgeService.getBadgeByUser(user);
        return new MypageInfoResponse(user, followerCount, followingCount, badge);
    }
}
