package com.newsvision.global.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentInitRequest {
    private String pg;
    private String payMethod;
    private Long productId;
    private String productName;
    private int price;
    private int quantity;

    private Integer subcriptionPeriod;
}