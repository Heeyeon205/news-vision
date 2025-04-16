package com.newsvision.news.entity;

import com.newsvision.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "news_likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news;
}