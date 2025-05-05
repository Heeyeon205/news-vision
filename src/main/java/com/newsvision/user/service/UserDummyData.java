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
    @Transactional
    CommandLineRunner initUsers() {
        String defaultProfile = "https://newsion-project.s3.ap-northeast-2.amazonaws.com/profiles/4/default-profile.png";
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                Badge adminBadge = badgeService.findByBadgeId(1L);
                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .nickname("뉴션")
                        .image(defaultProfile)
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
                        .password(passwordEncoder.encode("1234"))
                        .nickname("신대리")
                        .image(defaultProfile)
                        .email("creator@creator.com")
                        .role(User.Role.ROLE_CREATOR)
                        .badge(creatorBadge)
                        .build();
                userRepository.save(creator);
            }

            if (!userRepository.existsByUsername("creator2")) {
                Badge creatorBadge = badgeService.findByBadgeId(2L);
                User creator2 = User.builder()
                        .username("creator2")
                        .password(passwordEncoder.encode("1234"))
                        .nickname("부기")
                        .image(defaultProfile)
                        .email("creator2@creator.com")
                        .role(User.Role.ROLE_CREATOR)
                        .badge(creatorBadge)
                        .build();
                userRepository.save(creator2);
            }

            if (!userRepository.existsByUsername("creator3")) {
                Badge creatorBadge = badgeService.findByBadgeId(2L);
                User creator3 = User.builder()
                        .username("creator3")
                        .password(passwordEncoder.encode("1234"))
                        .nickname("책콩")
                        .image(defaultProfile)
                        .email("creator3@creator.com")
                        .role(User.Role.ROLE_CREATOR)
                        .badge(creatorBadge)
                        .build();
                userRepository.save(creator3);
            }

            if (!userRepository.existsByUsername("user")) {
                User user = User.builder()
                        .username("user")
                        .password(passwordEncoder.encode("1234"))
                        .nickname("쥰쓰")
                        .image(defaultProfile)
                        .email("user@user.com")
                        .role(User.Role.ROLE_USER)
                        .build();
                userRepository.save(user);
            }

            if (!userRepository.existsByUsername("user2")) {
                User user2 = User.builder()
                        .username("user2")
                        .password(passwordEncoder.encode("1234"))
                        .nickname("소이")
                        .image(defaultProfile)
                        .email("user2@user.com")
                        .role(User.Role.ROLE_USER)
                        .build();
                userRepository.save(user2);
            }

            if (!userRepository.existsByUsername("user3")) {
                User user3 = User.builder()
                        .username("user3")
                        .password(passwordEncoder.encode("1234"))
                        .nickname("하이아웃")
                        .image(defaultProfile)
                        .email("user3@user.com")
                        .role(User.Role.ROLE_USER)
                        .build();
                userRepository.save(user3);
            }

            if (!userRepository.existsByUsername("user4")) {
                User user4 = User.builder()
                        .username("user4")
                        .password(passwordEncoder.encode("1234"))
                        .nickname("파우치")
                        .image(defaultProfile)
                        .email("user4@user.com")
                        .role(User.Role.ROLE_USER)
                        .build();
                userRepository.save(user4);
            }

            if (!userRepository.existsByUsername("user5")) {
                User user5 = User.builder()
                        .username("user5")
                        .password(passwordEncoder.encode("1234"))
                        .nickname("플라이")
                        .image(defaultProfile)
                        .email("user5@user.com")
                        .role(User.Role.ROLE_USER)
                        .build();
                userRepository.save(user5);
            }

            if (!userRepository.existsByUsername("user6")) {
                User user6 = User.builder()
                        .username("user6")
                        .password(passwordEncoder.encode("1234"))
                        .nickname("케쳐")
                        .image(defaultProfile)
                        .email("user6@user.com")
                        .role(User.Role.ROLE_USER)
                        .build();
                userRepository.save(user6);
            }
        };
    }
}
