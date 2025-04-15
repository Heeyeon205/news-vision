package com.newsvision.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true, length = 12)
    private String nickname;
    @Column(name = "create_at")
    private LocalDateTime createAt;
    private String introduce;
    private String image;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Provider provider;
    @Column(name = "provider_id")
    private String providerId;
    @Column(name = "is_paid")
    private Boolean isPaid;

    @PrePersist
    public void prePersist() {
        this.createAt = this.createAt == null ? LocalDateTime.now() : this.createAt;
        this.role = this.role == null ? Role.USER : this.role;
        this.provider = this.provider == null ? Provider.LOCAL : this.provider;
        this.isPaid = this.isPaid == null ? false : this.isPaid;
    }

    public enum Role {
        USER, CREATOR, ADMIN
    }
    public enum Provider {
        LOCAL, GOOGLE, KAKAO, NAVER
    }
}
