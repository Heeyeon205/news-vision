package com.newsvision.poll.service;

import com.newsvision.board.dto.response.CommentResponse;
import com.newsvision.board.entity.Board;
import com.newsvision.board.entity.Comment;
import com.newsvision.board.service.BoardService;
import com.newsvision.board.service.CommentService;
import com.newsvision.notice.entity.Notice;
import com.newsvision.notice.service.NoticeService;
import com.newsvision.user.entity.User;
import com.newsvision.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardCommentManager {
    private final BoardService boardService;
    private final CommentService commentService;
    private final UserService userService;
    private final NoticeService noticeService;

    @Transactional
    public CommentResponse createComment(Long boardId, Long userId, String commentContent) {
        Board board = boardService.findById(boardId);
        User user = userService.findByUserId(userId);

        Comment comment = new Comment();
        comment.setBoard(board);
        comment.setUser(user);
        comment.setContent(commentContent);
        Comment savedComment = commentService.save(comment);


        User sender = userService.findByUserId(userId);
        User receiver = userService.findByUserId(board.getUser().getId());
        String url = "/board/" + board.getId();
        noticeService.createAndSendNotice(sender, receiver, Notice.Type.BOARD_COMMENT, url, "회원님의 게시글에 댓글을 달았습니다.");
        return new CommentResponse(savedComment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentService.findById(commentId);
        userService.matchUserId(comment.getUser().getId(), userId);
        commentService.delete(comment);
    }
}
