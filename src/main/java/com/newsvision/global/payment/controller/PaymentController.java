package com.newsvision.global.payment.controller;

import com.newsvision.global.payment.dto.OrderDto;
import com.newsvision.global.payment.dto.PaymentInitRequest;
import com.newsvision.global.payment.service.PaymentService;
import com.newsvision.global.security.CustomUserDetails;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/init")
    public ResponseEntity<Map<String, Object>> initiatePayment(
            @RequestBody PaymentInitRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (userDetails == null) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(401).body(response);
            }

            response.put("success", true);
            response.put("pg", request.getPg());
            response.put("pay_method", request.getPayMethod());
            response.put("merchant_uid", "ORD" + System.currentTimeMillis() + (int)(Math.random() * 1000000));
            response.put("name", request.getProductName());
            response.put("amount", request.getPrice());
            response.put("buyer_name", userDetails.getUsername());
            response.put("isPaid", userDetails.getUser().getIsPaid());
            response.put("subscription_period",request.getSubcriptionPeriod()); // 구독기간

            OrderDto orderDto = new OrderDto();
            orderDto.setProductId(request.getProductId());
            orderDto.setProductName(request.getProductName());
            orderDto.setPrice(request.getPrice());
            orderDto.setQuantity(request.getQuantity());
            orderDto.setMerchantUid((String) response.get("merchant_uid"));
            orderDto.setSubscriptionPeriod(request.getSubcriptionPeriod());

            log.info("임시 주문 저장: userId={}, orderDto={}", userDetails.getId(), orderDto);
            paymentService.storePendingOrder(userDetails.getId(), orderDto);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("결제 초기화 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "서버 내부 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }


    @PostMapping("/validation/{imp_uid}")
    public IamportResponse<Payment> validateIamport(
            @PathVariable String imp_uid,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws IamportResponseException, IOException {
        log.info("imp_uid: {}, userId: {}", imp_uid, userDetails.getId());
        return paymentService.validateIamport(imp_uid);
    }

    @PostMapping("/order")
    public ResponseEntity<String> processOrder(
            @RequestBody OrderDto orderDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        log.info("Received order: {}, userId: {}", orderDto.toString(), userDetails.getId());
        return ResponseEntity.ok(paymentService.saveOrder(orderDto, userDetails.getId()));
    }

    @PostMapping("/cancel/{imp_uid}")
    public IamportResponse<Payment> cancelPayment(
            @PathVariable String imp_uid,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws IamportResponseException, IOException {
        log.info("Cancel payment: {}, userId: {}", imp_uid, userDetails.getId());
        return paymentService.cancelPayment(imp_uid);
    }

    // 환불 요청 api
    @PostMapping("/refund/request/{imp_uid}")
    public ResponseEntity<String> requestRefund(
            @PathVariable String imp_uid,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        log.info("환불 요청: imp_uid={}, userId={}", imp_uid, userDetails.getId());
        return ResponseEntity.ok(paymentService.requestRefund(userDetails.getId(), imp_uid));
    }

    @PostMapping("/refund/approve/{imp_uid}")
    public ResponseEntity<String> approveRefund(
            @PathVariable("imp_uid") String impUid,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body("관리자 권한이 필요합니다.");
        }
        log.info("환불 승인 요청: imp_uid={}, userId={}", impUid, userDetails.getId());
        return ResponseEntity.ok(paymentService.approveRefund(userDetails.getId(), impUid));
    }

}