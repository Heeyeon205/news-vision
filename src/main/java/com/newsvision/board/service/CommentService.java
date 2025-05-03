package com.newsvision.board.service;

import com.newsvision.board.dto.response.CommentResponse;
import com.newsvision.board.entity.Board;
import com.newsvision.board.entity.Comment;
import com.newsvision.board.repository.CommentRepository;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.notice.entity.Notice;
import com.newsvision.notice.service.NoticeService;
import com.newsvision.user.entity.User;
import com.newsvision.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;

    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    public List<CommentResponse> getCommentsByBoardId(Long boardId) {
        List<Comment> comments = commentRepository.findByBoardId(boardId);
        return comments.stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }

    public int countByBoardId(Long id) {
        return commentRepository.countByBoardId(id);
    }

    @Transactional
    public Comment save(Comment comment){
        return commentRepository.save(comment);
    }

    @Transactional
    public CommentResponse updateComment (Long commentId, Long userId, String commentContent) {
        Comment comment = findById(commentId);
        userService.matchUserId(comment.getUser().getId(), userId);
        comment.setContent(commentContent);
        Comment updatedComment = commentRepository.save(comment);
        return new CommentResponse(updatedComment);

    }

    @Transactional
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }
}
