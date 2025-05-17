package com.newsvision.news.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "naver_news")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaverNews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false, unique = true)
    private String link;
    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @OneToMany(mappedBy = "naverNews", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<News> newsList;
}
