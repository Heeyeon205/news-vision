package com.newsvision.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "boards")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String title;
    @Column(nullable = false, length = 1000)
    private String content;
    @Column(name = "category_id",nullable = false)
    private Long categoryId;
    @Column(name = "create_at")
    private LocalDateTime createAt;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "image")
    private String image;
    @Column(name = "view")
    private Long view;
    @Column(name = "news_id")
    private Long newsId;
    @Column(name = "is_reported")
    private Boolean isReported;

    //BoardLike 엔티티와 관계 설정
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardLike> boardLikes;

    //Comment 엔티티와 관계 설정
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    // BoardReport 엔티티와 관계 설정
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardReport> boardReports;

}
