package com.newsvision.user.repository;

import com.newsvision.user.entity.Follow;
import com.newsvision.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    int countByFollowingId(Long userId);

    int countByFollowerId(Long userId);
}
