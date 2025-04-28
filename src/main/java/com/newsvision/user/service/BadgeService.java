package com.newsvision.user.service;

import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.user.entity.Badge;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.BadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BadgeService {
    private final BadgeRepository badgeRepository;

    public Badge getBadgeByUser(User user) {
       User.Role userRole = user.getRole();
        return switch (userRole) {
            case ROLE_ADMIN -> badgeRepository.findByRole(Badge.Role.ROLE_ADMIN);
            case ROLE_CREATOR -> badgeRepository.findByRole(Badge.Role.ROLE_CREATOR);
            default -> null;
        };
    }

    public Badge findByBadgeId(Long id) {
        return badgeRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }
}
