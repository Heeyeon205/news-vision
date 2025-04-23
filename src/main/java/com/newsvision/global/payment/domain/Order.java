package com.newsvision.global.payment.domain;

import com.newsvision.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long productId;
    String productName;
    int price;
    int quantity;
    String impUid;
    String merchantUid;

    @Column(name = "subscription_period")
    private Integer subscriptionPeriod;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id") // user_id 외래 키 설정
    private User user;
    @Column(name = "username")
    private String username;

}