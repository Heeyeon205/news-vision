package com.newsvision.global.payment.dto;

import com.newsvision.global.payment.domain.Order;
import com.newsvision.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDto {
    Long productId;
    String productName;
    int price;
    int quantity;
    String impUid;
    String merchantUid;

    Integer subscriptionPeriod;

    public Order toEntity(User user) {
        return Order.builder()
                .productId(productId)
                .productName(productName)
                .price(price)
                .quantity(quantity)
                .impUid(impUid)
                .merchantUid(merchantUid)
                .subscriptionPeriod(subscriptionPeriod)
                .paymentDate(LocalDateTime.now()) // 결제 날짜 설정
                .user(user)
                .username(user.getUsername())
                .build();
    }
}