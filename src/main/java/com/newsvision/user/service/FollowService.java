package com.newsvision.user.service;

import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.user.dto.response.UserFollowCountResponse;
import com.newsvision.user.entity.Follow;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.FollowRepository;
import com.newsvision.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;

    public int getCountFollower(User user) {
        return followRepository.countByFollowing(user);
    }

    public int getCountFollowing(User user) {
        return followRepository.countByFollower(user);
    }
}