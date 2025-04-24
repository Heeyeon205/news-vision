package com.newsvision.global.payment.service;

import com.newsvision.global.payment.domain.Order;
import com.newsvision.global.payment.domain.OrderRepository;
import com.newsvision.global.payment.domain.RefundRequest;
import com.newsvision.global.payment.domain.RefundRequestRepository;
import com.newsvision.global.payment.dto.OrderDto;
import com.newsvision.global.payment.dto.RefundRequestDto;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import com.newsvision.user.service.UserService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final IamportClient iamportClient;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RefundRequestRepository refundRequestRepository;
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
            User user = userService.findByUserId(userId);

            if (user.getIsPaid()) {
                return "이미 구독 중인 사용자입니다. 중복 결제는 허용되지 않습니다.";
            }

            if (orderDto.getSubscriptionPeriod() != 1 && orderDto.getSubscriptionPeriod() != 3 && orderDto.getSubscriptionPeriod() != 6) {
                return "지원되지 않는 구독권 기간입니다.";
            }

            Order order = orderDto.toEntity(user);
            orderRepository.save(order);
            userService.updateIsPaidOrUser(user);
            return "주문 정보가 성공적으로 저장되었습니다.";
        } catch (Exception e) {
            log.error("주문 저장 실패: {}", e.getMessage());
            cancelPayment(orderDto.getImpUid());
            return "주문 정보 저장에 실패했습니다.";
        }
    }

    //환불 가능여부 확인
    public boolean isRefundEligible(Order order) {
        if (order == null || order.getPaymentDate() == null || order.getSubscriptionPeriod() == null) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime paymentDate = order.getPaymentDate();
        long monthsPassed = ChronoUnit.MONTHS.between(paymentDate, now);

        return monthsPassed <= order.getSubscriptionPeriod();
    }

    // 환불 요청 처리 (관리자 승인 전)
    public String requestRefund(Long userId, String impUid) {
        try {
            User user = userService.findByUserId(userId);
            if (!user.getIsPaid()) {
                return "구독 중인 사용자가 아닙니다.";
            }

            Order order = orderRepository.findByImpUid(impUid)
                    .orElseThrow(() -> new IllegalArgumentException("해당 결제 정보를 찾을 수 없습니다."));

            if (!isRefundEligible(order)) {
                return "구독 기간이 만료되어 환불이 불가합니다.";
            }
            if (refundRequestRepository.findByImpUid(impUid).isPresent()) {
                return "이미 접수된 환불 요청입니다.";
            }

            // RefundRequest 저장
            RefundRequest refundRequest = RefundRequest.builder()
                    .impUid(impUid)
                    .user(user)
                    .status(RefundRequest.Status.PENDING)
                    .requestDate(LocalDateTime.now())
                    .build();
            refundRequestRepository.save(refundRequest);

            // 환불 요청 상태 저장 (예: 별도 테이블 또는 플래그)
            log.info("환불 요청 접수: userId={}, impUid={}", userId, impUid);
            return "환불 요청이 접수되었습니다. 관리자 승인을 기다려주세요.";
        } catch (Exception e) {
            log.error("환불 요청 처리 실패: {}", e.getMessage());
            return "환불 요청 처리 중 오류가 발생했습니다.";
        }
    }

    @Transactional
    // 관리자 승인 후 환불 처리
    public String approveRefund(Long adminUserId, String impUid) {
        try {
            log.info("조회 사용자 : userId={}, impUid={}", adminUserId, impUid);
            RefundRequest refundRequest = refundRequestRepository.findByImpUid(impUid)
                    .orElseThrow(() -> new IllegalArgumentException("환불 요청을 찾을 수 없습니다."));

            if(refundRequest.getStatus() != RefundRequest.Status.PENDING){
                return "이미 처리된 환불 요청 입니다.";
            }
            //order에서 환불 대상 사용자 조회
            Order order = orderRepository.findByImpUid(impUid)
                    .orElseThrow(() -> new IllegalArgumentException("해당 결제 정보를 찾을 수 없습니다."));

            User user = order.getUser();
            if(user == null){
                log.error("Order에 연결된 사용자가 없습니다: impUid={}", impUid);
                throw new IllegalStateException("환불 대상 사용자를 찾을 수 없습니다.");
            }
            log.info("조회된 환불 대상 사용자: userId={}, isPaid={}", user.getId(), user.getIsPaid());

            if (!isRefundEligible(order)) {
                refundRequest.updateStatus(RefundRequest.Status.REJECTED);// 거절
                refundRequestRepository.save(refundRequest);
                log.info("환불 불가: 구독 기간 초과, paymentDate={}, subscriptionPeriod={}",
                        order.getPaymentDate(), order.getSubscriptionPeriod());
                return "구독 기간이 만료되어 환불이 불가합니다.";
            }

            // isPaid를 false로 변경
            user.updateIsPaid(false);
            log.info("isPaid 변경 후: isPaid={}", user.getIsPaid());
            if (user.getIsPaid() == null || user.getIsPaid()) {
                log.error("isPaid 변경 실패: isPaid={}", user.getIsPaid());
                throw new IllegalStateException("isPaid 변경에 실패했습니다.");
            }

            userRepository.save(user);
            log.info("userRepository.save 호출 완료: userId={}", user.getId());

            //order 삭제
            orderRepository.delete(order);
            log.info("Order 삭제 완료: impUid={}", impUid);

            refundRequest.updateStatus(RefundRequest.Status.APPROVED);//승인
            refundRequestRepository.save(refundRequest);
            log.info("RefundRequest 상태 업데이트: impUid={}, status=APPROVED", impUid);
            // 포트원 관리자가 수동으로 취소해야 함
            log.info("환불 승인 완료: userId={}, impUid={}. 포트원 관리자가 수동으로 결제를 취소해야 합니다.", user.getId(), impUid);
            return "환불이 승인되었습니다. 포트원 관리자가 결제를 취소할 예정입니다.";
        } catch (Exception e) {
            log.error("환불 승인 처리 실패: {}", e.getMessage() , e);
            return "환불 승인 처리 중 오류가 발생했습니다.";
        }
    }
    // 관리자 환불 거부
    @Transactional
    public String rejectRefund(Long adminUserId, String impUid) {
        try {
            log.info("환불 거부 요청: adminUserId={}, impUid={}", adminUserId, impUid);

            RefundRequest refundRequest = refundRequestRepository.findByImpUid(impUid)
                    .orElseThrow(() -> new IllegalArgumentException("환불 요청을 찾을 수 없습니다."));

            if (refundRequest.getStatus() != RefundRequest.Status.PENDING) {// 보류
                return "이미 처리된 환불 요청입니다.";
            }

            refundRequest.updateStatus(RefundRequest.Status.REJECTED); // 거절
            refundRequestRepository.save(refundRequest);
            log.info("RefundRequest 상태 업데이트: impUid={}, status=REJECTED", impUid);

            return "환불 요청이 거부되었습니다.";
        } catch (Exception e) {
            log.error("환불 거부 처리 실패: {}", e.getMessage(), e);
            return "환불 거부 처리 중 오류가 발생했습니다.";
        }
    }
    @Transactional(readOnly = true)
    public List<RefundRequestDto> getRefundRequests() {
        List<RefundRequest> requests = refundRequestRepository.findAll();
        return requests.stream()
                .map(request -> RefundRequestDto.builder()
                        .id(request.getId())
                        .impUid(request.getImpUid())
                        .username(request.getUser().getUsername())
                        .status(request.getStatus().name())
                        .requestDate(request.getRequestDate())
                        .processedDate(request.getProcessedDate())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RefundRequestDto> getMaxRefundRequests() {
        List<RefundRequest> requests = refundRequestRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return requests.stream()
                .map(request -> RefundRequestDto.builder()
                        .id(request.getId())
                        .impUid(request.getImpUid())
                        .username(request.getUser().getUsername())
                        .status(request.getStatus().name())
                        .requestDate(request.getRequestDate())
                        .processedDate(request.getProcessedDate())
                        .build())
                .collect(Collectors.toList());
    }
}