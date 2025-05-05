package com.newsvision.board.entity;

import com.newsvision.category.Categories;
import com.newsvision.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "boards")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,columnDefinition = "TEXT")
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Categories category;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id") // user_id 외래 키 설정
    private User user;
    @Column(name = "image")
    private String image;
    @Column(name = "view")
    private int view;
    @Column(name = "news_id")
    private Long newsId;
    @Column(name = "is_reported")
    private Boolean isReported;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardLike> boardLikes;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardReport> boardReports;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.view = this.view == 0 ? 0 : this.view;
        this.isReported = this.isReported == null ? false : this.isReported;
    }

    public void updateContent(String content){this.content = content;}
    public void updateCategory(Categories category){this.category = category;}
    public void updateImage(String image){this.image = image;}
    public void updateView(int view) {
        this.view = view;
    }
    public void updateIsReported(Boolean isReported) { this.isReported = isReported; }
}
