package com.newsvision.user.service;

import com.newsvision.user.entity.Badge;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class UserDummyData {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BadgeService badgeService;

    @Bean
    CommandLineRunner initUsers() {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                Badge adminBadge = badgeService.findByBadgeId(1L);
                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .nickname("뉴션")
                        .image("https://newsion-project.s3.ap-northeast-2.amazonaws.com/profiles/4/default-profile.png")
                        .email("admin@admin.com")
                        .role(User.Role.ROLE_ADMIN)
                        .badge(adminBadge)
                        .build();
                userRepository.save(admin);
            }

            if (!userRepository.existsByUsername("creator")) {
                Badge creatorBadge = badgeService.findByBadgeId(2L);
                User creator = User.builder()
                        .username("creator")
                        .password(passwordEncoder.encode("creator"))
                        .nickname("크리에이터")
                        .image("https://newsion-project.s3.ap-northeast-2.amazonaws.com/profiles/4/default-profile.png")
                        .email("creator@creator.com")
                        .role(User.Role.ROLE_CREATOR)
                        .badge(creatorBadge)
                        .build();
                userRepository.save(creator);
            }

            if (!userRepository.existsByUsername("qwer")) {
                User user = User.builder()
                        .username("qwer")
                        .password(passwordEncoder.encode("1234"))
                        .nickname("홍길동")
                        .image("https://newsion-project.s3.ap-northeast-2.amazonaws.com/profiles/4/default-profile.png")
                        .email("qwe@qwer.com")
                        .role(User.Role.ROLE_USER)
                        .build();
                userRepository.save(user);
            }
        };
    }
}
