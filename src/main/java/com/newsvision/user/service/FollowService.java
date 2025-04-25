package com.newsvision.user.service;

import com.newsvision.mypage.dto.response.FollowResponse;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {
    private final FollowRepository followRepository;
    private final UserService userService;

    public int getCountFollower(User user) {
        return followRepository.countByFollowing(user);
    }

    public int getCountFollowing(User user) {
        return followRepository.countByFollower(user);
    }
}