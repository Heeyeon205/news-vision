package com.newsvision.board.repository;

import com.newsvision.board.entity.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {
    boolean existsByCommentIdAndUserId(Long commentId, Long userId);
}
