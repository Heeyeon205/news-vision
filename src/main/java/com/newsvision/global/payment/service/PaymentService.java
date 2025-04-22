package com.newsvision.global.payment.service;

import com.newsvision.global.payment.domain.Order;
import com.newsvision.global.payment.domain.OrderRepository;
import com.newsvision.global.payment.dto.OrderDto;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final IamportClient iamportClient;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    // 임시 주문 데이터를 저장할 Map
    private final Map<String, OrderDto> pendingOrders = new HashMap<>();

    // 임시 주문 저장 메서드
    public void storePendingOrder(Long userId, OrderDto orderDto) {
        String key = userId + ":" + orderDto.getMerchantUid();
        pendingOrders.put(key, orderDto);
        log.info("임시 주문 저장됨: key={}, orderDto={}", key, orderDto);
    }

    // 임시 주문 조회 메서드
    public OrderDto getPendingOrder(Long userId, String merchantUid) {
        String key = userId + ":" + merchantUid;
        OrderDto orderDto = pendingOrders.get(key);
        if (orderDto != null) {
            pendingOrders.remove(key);
            log.info("임시 주문 조회 및 삭제됨: key={}", key);
        }
        return orderDto;
    }

    public IamportResponse<Payment> validateIamport(String imp_uid) {
        try {
            IamportResponse<Payment> payment = iamportClient.paymentByImpUid(imp_uid);
            log.info("결제 요청 응답. 결제 내역 - 주문 번호: {}", payment.getResponse());
            return payment;
        } catch (Exception e) {
            log.error("결제 검증 실패: {}", e.getMessage());
            return null;
        }
    }

    public IamportResponse<Payment> cancelPayment(String imp_uid) {
        try {
            CancelData cancelData = new CancelData(imp_uid, true);
            IamportResponse<Payment> payment = iamportClient.cancelPaymentByImpUid(cancelData);
            return payment;
        } catch (Exception e) {
            log.error("결제 취소 실패: {}", e.getMessage());
            return null;
        }
    }

    public String saveOrder(OrderDto orderDto, Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID: " + userId));

            if (user.getIsPaid()) {
                return "이미 결제된 사용자입니다. 중복 결제는 허용되지 않습니다.";
            }

            Order order = orderDto.toEntity(user);
            orderRepository.save(order);

            user.updateIsPaid(true);
            userRepository.save(user);
            return "주문 정보가 성공적으로 저장되었습니다.";
        } catch (Exception e) {
            log.error("주문 저장 실패: {}", e.getMessage());
            cancelPayment(orderDto.getImpUid());
            return "주문 정보 저장에 실패했습니다.";
        }
    }
}