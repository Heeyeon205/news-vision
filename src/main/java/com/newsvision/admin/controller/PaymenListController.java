package com.newsvision.admin.controller;

import com.newsvision.admin.service.PayDeleteService;
import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.payment.dto.RefundRequestDto;
import com.newsvision.global.payment.service.PaymentService;
import com.newsvision.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Hidden
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

    @DeleteMapping("/delete/refund/requests/{id}")
    public ResponseEntity<ApiResponse<String>> deleteRefundRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        payDeleteService.deleteRefundRequest(id);
        return ResponseEntity.ok(ApiResponse.success("환불 요청(ID: " + id + ") 삭제 완료"));
    }



    @GetMapping("/refund/requests/max")
    public ResponseEntity<ApiResponse<List<RefundRequestDto>>> getMaxRefundRequests(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.ok(ApiResponse.success(paymentService.getMaxRefundRequests()));
    }
}
