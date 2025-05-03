package com.newsvision.notice.repository;

import com.newsvision.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findByReceiverIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Page<Notice> findByReceiver_IdOrderByIdDesc(Long id, Pageable pageable);
}
