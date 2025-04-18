package com.newsvision.user.service;

import com.newsvision.user.dto.response.UserFollowCountResponse;
import com.newsvision.user.entity.Follow;
import com.newsvision.user.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;

//    public UserFollowCountResponse getFollowCount(Long userId) {
//        int followerCount = followRepository.countByFollowingId(userId);
//        int followingCount = followRepository.countByFollowerId(userId);
//        return new UserFollowCountResponse(followerCount, followingCount);
//    }
}
