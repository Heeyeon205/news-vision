package com.newsvision.user.repository;

import com.newsvision.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByNickname(String nickname);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAllByIsDeletedFalse();
    Optional<User> findByUsernameAndIsDeletedFalse(String username);
    Optional<User> findByIdAndIsDeletedFalse(Long userId);
}
