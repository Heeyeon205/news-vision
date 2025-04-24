package com.newsvision.user.service;

import com.newsvision.user.dto.response.FollowResponse;
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

   public List<FollowResponse> getFollowers(Long id){
        User user = userService.findByUserId(id);
       return user.getFollowerList().stream()
                .map(follow -> FollowResponse.from(follow.getFollower()))
                .toList();
   }

    public List<FollowResponse> getFollowing(Long id){
        User user = userService.findByUserId(id);
        return user.getFollowingList().stream()
                .map(follow -> FollowResponse.from(follow.getFollowing()))
                .toList();
    }
}