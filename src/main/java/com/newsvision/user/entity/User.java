package com.newsvision.user.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.newsvision.board.entity.Board;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.news.entity.News;
import com.newsvision.news.entity.Scrap;
import com.newsvision.notice.Notice;
import com.newsvision.user.dto.request.UpdateUserRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column()
    private String email;
    @Column(nullable = false, unique = true)
    private String nickname;
    @Column(name = "create_at")
    private LocalDateTime createAt;
    private String introduce;
    private String image;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id")
    private Badge badge;
    @Enumerated(EnumType.STRING)
    private Provider provider;
    @Column(name = "provider_id")
    private String providerId;
    @Column(name = "is_paid")
    private Boolean isPaid;

    @PrePersist
    public void prePersist() {
        this.createAt = this.createAt == null ? LocalDateTime.now() : this.createAt;
        this.role = this.role == null ? Role.ROLE_USER : this.role;
        this.provider = this.provider == null ? Provider.LOCAL : this.provider;
        this.isPaid = this.isPaid == null ? false : this.isPaid;
    }

    public enum Role {
        ROLE_USER, ROLE_CREATOR, ROLE_ADMIN
    }
    public enum Provider {
        LOCAL, GOOGLE, KAKAO, NAVER
    }

    public void updateImage(String image) {
        log.warn("업데이트할 이미지 URL: {}", image);
        this.image = image;
    }
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
    public void updateIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void validateBadgeRequirement() {
        if ((this.role == Role.ROLE_ADMIN || this.role == Role.ROLE_CREATOR) && this.badge == null) {
            throw new CustomException(ErrorCode.BADGE_REQUIRED);
        }
    }

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-follwers")
    private List<Follow> followerList = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-follwings")
    private List<Follow> followingList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-news")
    private List<News> newsList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-board")
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-scraps")
    private List<Scrap> scrapList = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notice> noticeList = new ArrayList<>();

}
