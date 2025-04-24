package com.newsvision.admin.controller;

import ch.qos.logback.classic.Logger;
import com.newsvision.admin.controller.response.BoardReportResponse;
import com.newsvision.admin.service.PayDeleteService;
import com.newsvision.global.payment.dto.RefundRequestDto;
import com.newsvision.global.payment.service.PaymentService;
import com.newsvision.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin(origins = "*")

public class PaymenListController {

    private final PaymentService paymentService;
    private final PayDeleteService payDeleteService;

    @Autowired
    public PaymenListController(PaymentService paymentService, PayDeleteService payDeleteService) {
        this.paymentService = paymentService;
        this.payDeleteService = payDeleteService;
    }

    //삭제
    @DeleteMapping("/delete/refund/requests/{id}")
    public ResponseEntity<Void> deleteRefundRequest(@PathVariable Long id) {
        payDeleteService.deleteRefundRequest(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }


    //id 최신순
    @GetMapping("/refund/requests/max")
    public ResponseEntity<List<RefundRequestDto>> getMaxRefundRequests(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.ok(paymentService.getMaxRefundRequests());
    }
}
