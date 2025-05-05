package com.newsvision.poll.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "polls_options")
public class PollOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", referencedColumnName = "id", nullable = false)
    private Poll poll;

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateCount(int count) {
        this.count = count;
    }
}
