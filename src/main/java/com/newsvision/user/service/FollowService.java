package com.newsvision.user.service;

import com.newsvision.mypage.dto.response.FollowResponse;
import com.newsvision.notice.entity.Notice;
import com.newsvision.notice.service.NoticeService;
import com.newsvision.user.entity.Follow;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {
    private final FollowRepository followRepository;
    private final UserService userService;
    private final NoticeService noticeService;

    public int getCountFollower(User user) {
        return followRepository.countByFollowing(user);
    }

    public int getCountFollowing(User user) {
        return followRepository.countByFollower(user);
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

        String url = "/userPage/" + myId;
        noticeService.createAndSendNotice(my, target, Notice.Type.FOLLOW, url, "회원님을 팔로우하기 시작했습니다.");
    }

    @Transactional
    public void unFollow(Long myId, Long targetId) {
        followRepository.deleteByFollower_IdAndFollowing_Id(myId, targetId);
    }

    public boolean existsFollow(Long followerId, Long followingId) {
        return followRepository.existsByFollower_IdAndFollowing_Id(followerId, followingId);
    }

    public Page<Follow> findByFollowingId(Long id, Pageable pageable) {
        return followRepository.findByFollowingId(id, pageable);
    }

    public Page<Follow> findByFollowerId(Long id, Pageable pageable) {
        return followRepository.findByFollowerId(id, pageable);
    }
}