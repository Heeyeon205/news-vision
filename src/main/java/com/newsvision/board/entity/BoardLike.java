package com.newsvision.board.entity;


import com.newsvision.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "boardLikes")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false) // user_id 외래 키 설정
    private User user;

    // Board 엔티티와의 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    private Board board;

}
