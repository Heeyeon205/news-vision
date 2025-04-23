package com.newsvision.global.payment.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefundRequestRepository extends JpaRepository<RefundRequest, Long> {
    Optional<RefundRequest> findByImpUid(String impUid);
    List<RefundRequest> findAllByStatus(RefundRequest.Status status);
}
