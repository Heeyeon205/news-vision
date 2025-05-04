package com.newsvision.news.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.newsvision.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "scraps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("user-scraps")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
