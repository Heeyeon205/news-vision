package com.newsvision.global.payment.domain;


import com.newsvision.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refund_requests")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imp_uid" , nullable = false)
    private String impUid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;

    @Column(name = "processed_date")
    private LocalDateTime processedDate;

    public enum Status { // 승인 대기 취소
        PENDING, APPROVED, REJECTED
    }

    public void updateStatus(Status status) {
        this.status = status;
        this.processedDate = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        this.requestDate = this.requestDate == null ? LocalDateTime.now() : this.requestDate;
        this.status = this.status == null ? Status.PENDING : this.status;
    }
}
