package com.newsvision.admin.service;

import com.newsvision.global.payment.domain.RefundRequest;
import com.newsvision.global.payment.domain.RefundRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PayDeleteService {
    private final RefundRequestRepository refundRequestRepository;

    //삭제 서버스
    public void deleteRefundRequest(Long id) {
        RefundRequest request = refundRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 환불 요청이 존재하지 않습니다. id=" + id));
        refundRequestRepository.delete(request);
    }
}
