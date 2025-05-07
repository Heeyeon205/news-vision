package com.newsvision.board.service;


import com.newsvision.board.entity.Board;
import com.newsvision.board.entity.BoardReport;
import com.newsvision.board.entity.Comment;
import com.newsvision.board.entity.CommentReport;
import com.newsvision.board.repository.BoardReportRepository;
import com.newsvision.board.repository.CommentReportRepository;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.notice.service.NoticeService;
import com.newsvision.user.entity.User;
import com.newsvision.user.service.UserService;
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
    private final UserService userService;
    private final BoardService boardService;
    private final CommentService commentService;

    @Transactional
    public void reportBoard(Long boardId, Long userId) {
        if (boardReportRepository.existsByBoardIdAndUserId(boardId, userId)) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        Board board = boardService.findById(boardId);
        User user = userService.findByUserId(userId);
        BoardReport boardReport = BoardReport.builder()
                    .board(board)
                    .user(user)
                    .build();
        boardReportRepository.save(boardReport);
    }

    @Transactional
    public void reportComment(Long commentId, Long userId) {
        boolean alreadyReported = commentReportRepository.existsByCommentIdAndUserId(commentId, userId);
        if (alreadyReported) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        Comment comment = commentService.findById(commentId);
        User user = userService.findByUserId(userId);

        CommentReport commentReport = CommentReport.builder()
                .comment(comment)
                .user(user)
                .build();
        commentReportRepository.save(commentReport);
    }

}
