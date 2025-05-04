package com.newsvision.admin.service;

import com.newsvision.admin.dto.response.BoardReportResponse;
import com.newsvision.board.entity.Board;
import com.newsvision.board.entity.BoardReport;
import com.newsvision.board.repository.BoardReportRepository;
import com.newsvision.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardReportService {
    private final BoardReportRepository boardReportRepository;
    private final BoardRepository boardRepository;

    public List<BoardReportResponse> getAllReports() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return boardReportRepository.findAll().stream()
                .map(report -> {
                    Board board = report.getBoard();
                    return BoardReportResponse.builder()
                            .id(report.getId())
                            .boardId(board.getId())
                            .boardWriter(board.getUser().getNickname())
                            .boardCreatedAt(board.getCreatedAt().format(formatter))
                            .userNickname(report.getUser().getNickname())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public void deleteCategory(Long id) {
        boardReportRepository.deleteById(id);
    }

    @Transactional
    public void markBoardAsReported(Long reportId) {
        BoardReport report = boardReportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고 ID 없음: " + reportId));

        Board board = report.getBoard();
        board.setIsReported(true);
        board.setContent("관리자로 인해 삭제된 게시글 입니다.");
        boardRepository.save(board);
        boardReportRepository.deleteByBoard(board);
    }
}