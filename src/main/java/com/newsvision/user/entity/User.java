package com.newsvision.user.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.newsvision.board.entity.Board;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.news.entity.News;
import com.newsvision.news.entity.Scrap;
import com.newsvision.notice.entity.Notice;
import com.newsvision.poll.entity.Poll;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Where;

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
    @Column(unique = true)
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id")
    private Badge badge;
    @Enumerated(EnumType.STRING)
    private Provider provider;
    @Column(name = "provider_id")
    private String providerId;
    @Column(name = "is_paid")
    private Boolean isPaid;
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @PrePersist
    public void prePersist() {
        this.createAt = this.createAt == null ? LocalDateTime.now() : this.createAt;
        this.role = this.role == null ? Role.ROLE_USER : this.role;
        this.provider = this.provider == null ? Provider.LOCAL : this.provider;
        this.isPaid = this.isPaid == null ? false : this.isPaid;
    }

    public void updateRole(Badge creatorBadge) {
        if (this.role == Role.ROLE_USER) {
            this.role = Role.ROLE_CREATOR;
            this.badge = creatorBadge; // 크리에이터용 배지 할당
        } else if (this.role == Role.ROLE_CREATOR) {
            this.role = Role.ROLE_USER;
            this.badge = null;
        } else {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    public void stateIsDeleted(boolean b) {
        this.isDeleted = b;
    }

    public enum Role {
        ROLE_USER, ROLE_CREATOR, ROLE_ADMIN
    }
    public enum Provider {
        LOCAL, GOOGLE, KAKAO, NAVER
    }

    public void updateImage(String image) { this.image = image; }
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
    public void updateEmail(String email) { this.email = email; }
    public void updateIntroduce(String introduce) {
        this.introduce = introduce;
    }
    public void updatePassword(String password) { this.password = password; }
    public void updateIsPaid(Boolean isPaid) { this.isPaid = isPaid; }

    public void validateBadgeRequirement() {
        if ((this.role == Role.ROLE_ADMIN || this.role == Role.ROLE_CREATOR) && this.badge == null) {
            throw new CustomException(ErrorCode.BADGE_REQUIRED);
        }
    }

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-followers")
    private List<Follow> followerList;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-followings")
    private List<Follow> followingList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-news")
    private List<News> newsList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-board")
    private List<Board> boardList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-scraps")
    private List<Scrap> scrapList;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notice> noticeList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-polls")
    private List<Poll> pollList;
}
