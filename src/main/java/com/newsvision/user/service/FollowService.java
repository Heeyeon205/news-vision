package com.newsvision.user.service;

import com.newsvision.user.entity.Follow;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public boolean isFollowing(Long myId, Long targetId) {
        // 내가 저 사람을 팔로우하고 있는지? →
        return followRepository.existsByFollower_IdAndFollowing_Id(myId, targetId);
    }

    public boolean isFollowedBy(Long myId, Long targetId) {
        // 저 사람이 나를 팔로우하고 있는지?
        return followRepository.existsByFollower_IdAndFollowing_Id(targetId, myId);
    }

    @Transactional
    public void follow(Long myId, Long targetId) {

        User my = userService.findByUserId(myId);
        User target = userService.findByUserId(targetId);
        Follow follow = Follow.builder()
                .follower(my)
                .following(target)
                .build();
        followRepository.save(follow);
    }

    @Transactional
    public void unFollow(Long myId, Long targetId) {
        followRepository.deleteByFollower_IdAndFollowing_Id(myId, targetId);
    }

    public boolean existsFollow(Long followerId, Long followingId) {
        return followRepository.existsByFollower_IdAndFollowing_Id(followerId, followingId);
    }
}