package com.newsvision.board.repository;

import com.newsvision.board.entity.BoardReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardReportRepository extends JpaRepository<BoardReport, Long> {
    boolean existsByBoardIdAndUserId(Long boardId, Long userId);
}
