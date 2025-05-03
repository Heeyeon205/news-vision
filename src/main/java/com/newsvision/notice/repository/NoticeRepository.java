package com.newsvision.notice.repository;

import com.newsvision.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findByReceiverId(Long userId);
}
