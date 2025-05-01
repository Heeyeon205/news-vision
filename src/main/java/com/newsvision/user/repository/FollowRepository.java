package com.newsvision.user.repository;

import com.newsvision.user.entity.Follow;
import com.newsvision.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    int countByFollowing(User following);
    int countByFollower(User follower);

    List<Follow> findByFollowingId(Long id);
    List<Follow> findByFollowerId(Long id);

    boolean existsByFollowing_Id(Long userId);

    boolean existsByFollower_Id(Long userId);

    boolean existsByFollower_IdAndFollowing_Id(Long targetId, Long myId);

    void deleteByFollower_IdAndFollowing_Id(Long myId, Long targetId);
}
