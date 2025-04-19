package com.newsvision.board.entity;

import com.newsvision.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false) // user_id 외래 키 설정
    private User user;

    @Column(name = "is_reported")
    private Boolean isReported;

    @Column(nullable = false, length = 100)
    private String content;

    //Board 엔티티와의 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    private Board board;

    // CommentReport와의 관계 설정
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentReport> commentReports;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @PrePersist // 엔티티가 persist 되기 전에 실행
    public void prePersist() {
        this.isReported = this.isReported == null ? false : this.isReported; // isReported 초기값 설정 (null이면 false로)
        this.createAt = LocalDateTime.now();
    }
}
