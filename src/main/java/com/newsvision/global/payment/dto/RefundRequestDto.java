package com.newsvision.global.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundRequestDto {
    private Long id;
    private String impUid;
    private String username;
    private String status;
    private LocalDateTime requestDate;
    private LocalDateTime processedDate;
}
