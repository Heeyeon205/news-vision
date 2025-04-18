package com.newsvision.user.repository;

import com.newsvision.user.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Badge findByRole(Badge.Role role);
}
