package com.newsvision.board.entity;

import com.newsvision.user.entity.User;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id") // user_id 외래 키 설정
    private User user;
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

    @PrePersist // 엔티티가 persist 되기 전에 실행
    public void prePersist() {
        this.createAt = LocalDateTime.now();
        this.view = this.view == null ? 0 : this.view; // view 초기값 설정 (null이면 0으로)
        this.isReported = this.isReported == null ? false : this.isReported; // isReported 초기값 설정 (null이면 false로)
    }
}
