package com.newsvision.board.service;


import com.newsvision.board.entity.Board;
import com.newsvision.board.entity.BoardReport;
import com.newsvision.board.entity.Comment;
import com.newsvision.board.entity.CommentReport;
import com.newsvision.board.repository.BoardReportRepository;
import com.newsvision.board.repository.BoardRepository;
import com.newsvision.board.repository.CommentReportRepository;
import com.newsvision.board.repository.CommentRepository;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final BoardReportRepository boardReportRepository;
    private final CommentReportRepository commentReportRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public void reportBoard(Long boardId, Long userId) {
        boolean alreadyReported = boardReportRepository.existsByBoardIdAndUserId(boardId, userId);
        if (alreadyReported) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        BoardReport boardReport = new BoardReport();
        boardReport.setBoard(board);
        boardReport.setUser(user);
        boardReportRepository.save(boardReport);
    }

    @Transactional
    public void reportComment(Long commentId, Long userId) {
        boolean alreadyReported = commentReportRepository.existsByCommentIdAndUserId(commentId, userId);
        if (alreadyReported) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        CommentReport commentReport = new CommentReport();
        commentReport.setComment(comment);
        commentReport.setUser(user);
        commentReportRepository.save(commentReport);
    }

}
