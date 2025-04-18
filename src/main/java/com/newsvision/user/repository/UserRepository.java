package com.newsvision.user.repository;

import com.newsvision.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByNickname(String nickname);
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}
