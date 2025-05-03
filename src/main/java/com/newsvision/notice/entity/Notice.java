package com.newsvision.notice.entity;

import com.newsvision.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notice")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User senderId;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String url;

    private String title;

    @Column(name = "is_read")
    private boolean isRead;

    private LocalDateTime createdAt;

    public void updateRead(boolean answer) {
        this.isRead = answer;
    }

    public enum Type {
        NEWS_SCRAP, NEWS_LIKE,
        BOARD_LIKE, BOARD_COMMENT, BOARD_REPORT, COMMENT_REPORT,
        FOLLOW
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
