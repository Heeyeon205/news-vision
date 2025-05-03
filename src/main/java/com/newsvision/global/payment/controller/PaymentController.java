package com.newsvision.global.payment.controller;

import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.global.payment.dto.OrderDto;
import com.newsvision.global.payment.dto.PaymentInitRequest;
import com.newsvision.global.payment.dto.RefundRequestDto;
import com.newsvision.global.payment.service.PaymentService;
import com.newsvision.global.security.CustomUserDetails;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "결제 컨트롤러", description = "결제 및 환불 관련 API")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제 초기화", description = "결제를 위한 임시 주문 정보를 저장하고 응답 정보를 반환합니다.")
    @PostMapping("/init")
    public ResponseEntity<ApiResponse<Map<String, Object>>> initiatePayment(
            @RequestBody PaymentInitRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (userDetails == null) {
                return ResponseEntity.status(401).body(ApiResponse.fail(ErrorCode.INVALID_ACCESS_TOKEN));
            }

            response.put("success", true);
            response.put("pg", request.getPg());
            response.put("pay_method", request.getPayMethod());
            response.put("merchant_uid", "ORD" + System.currentTimeMillis() + (int)(Math.random() * 1000000));
            response.put("name", request.getProductName());
            response.put("amount", request.getPrice());
            response.put("buyer_name", userDetails.getUsername());
            response.put("isPaid", userDetails.getUser().getIsPaid());
            response.put("subscription_period", request.getSubcriptionPeriod());

            OrderDto orderDto = new OrderDto();
            orderDto.setProductId(request.getProductId());
            orderDto.setProductName(request.getProductName());
            orderDto.setPrice(request.getPrice());
            orderDto.setQuantity(request.getQuantity());
            orderDto.setMerchantUid((String) response.get("merchant_uid"));
            orderDto.setSubscriptionPeriod(request.getSubcriptionPeriod());

            log.info("임시 주문 저장: userId={}, orderDto={}", userDetails.getId(), orderDto);
            paymentService.storePendingOrder(userDetails.getId(), orderDto);

            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            log.error("결제 초기화 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "서버 내부 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(500).body(ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "결제 검증", description = "아임포트 imp_uid를 통해 결제 내역을 검증합니다.")
    @PostMapping("/validation/{imp_uid}")
    public IamportResponse<Payment> validateIamport(
            @PathVariable String imp_uid,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws IamportResponseException, IOException {
        log.info("imp_uid: {}, userId: {}", imp_uid, userDetails.getId());
        return paymentService.validateIamport(imp_uid);
    }

    @Operation(summary = "주문 최종 처리", description = "결제가 완료된 주문 정보를 저장합니다.")
    @PostMapping("/order")
    public ResponseEntity<ApiResponse<String>> processOrder(
            @RequestBody OrderDto orderDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        log.info("Received order: {}, userId: {}", orderDto.toString(), userDetails.getId());
        String result = paymentService.saveOrder(orderDto, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "결제 취소", description = "아임포트 결제건을 취소 요청합니다.")
    @PostMapping("/cancel/{imp_uid}")
    public IamportResponse<Payment> cancelPayment(
            @PathVariable String imp_uid,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws IamportResponseException, IOException {
        log.info("Cancel payment: {}, userId: {}", imp_uid, userDetails.getId());
        return paymentService.cancelPayment(imp_uid);
    }

    @Operation(summary = "환불 요청", description = "유저가 결제한 내역에 대해 환불을 요청합니다.")
    @PostMapping("/refund/request/{imp_uid}")
    public ResponseEntity<ApiResponse<String>> requestRefund(
            @PathVariable("imp_uid") String impUid,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.fail(ErrorCode.INVALID_ACCESS_TOKEN));
        }
        log.info("환불 요청: imp_uid={}, userId={}", impUid, userDetails.getId());
        String result = paymentService.requestRefund(userDetails.getId(), impUid);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "환불 승인", description = "관리자가 환불 요청을 승인합니다.")
    @PostMapping("/refund/approve/{imp_uid}")
    public ResponseEntity<ApiResponse<String>> approveRefund(
            @PathVariable("imp_uid") String impUid,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(ApiResponse.fail(ErrorCode.FORBIDDEN));
        }
        log.info("환불 승인 요청: imp_uid={}, userId={}", impUid, userDetails.getId());
        String result = paymentService.approveRefund(userDetails.getId(), impUid);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "환불 거부", description = "관리자가 환불 요청을 거부합니다.")
    @PostMapping("/refund/reject/{imp_uid}")
    public ResponseEntity<ApiResponse<String>> rejectRefund(
            @PathVariable("imp_uid") String impUid,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(ApiResponse.fail(ErrorCode.FORBIDDEN));
        }
        log.info("환불 거부 요청: imp_uid={}, adminUserId={}", impUid, userDetails.getId());
        String result = paymentService.rejectRefund(userDetails.getId(), impUid);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "환불 요청 리스트 조회", description = "관리자가 모든 환불 요청 내역을 조회합니다.")
    @GetMapping("/refund/requests")
    public ResponseEntity<ApiResponse<List<RefundRequestDto>>> getRefundRequests(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(ApiResponse.fail(ErrorCode.FORBIDDEN));
        }
        log.info("환불 요청 리스트 조회: adminUserId={}", userDetails.getId());
        List<RefundRequestDto> refundRequests = paymentService.getRefundRequests();
        return ResponseEntity.ok(ApiResponse.success(refundRequests));
    }
}
