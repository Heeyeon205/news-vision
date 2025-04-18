package com.newsvision.user.service;

import com.newsvision.user.entity.Badge;
import com.newsvision.user.repository.BadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BadgeService {
    private final BadgeRepository badgeRepository;

    public Badge getBadgeByRole(String role) {
                return badgeRepository.findByRole(Badge.Role.valueOf(role));
    }
}
