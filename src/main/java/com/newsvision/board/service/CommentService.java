package com.newsvision.board.service;

import com.newsvision.board.controller.response.CommentResponse;
import com.newsvision.board.entity.Board;
import com.newsvision.board.entity.Comment;
import com.newsvision.board.repository.BoardRepository;
import com.newsvision.board.repository.CommentRepository;
import com.newsvision.global.Utils.TimeUtil;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
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
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public List<CommentResponse> getCommentsByBoardId(Long boardId) { // 특정 게시글 댓글 목록 조회
        List<Comment> comments = commentRepository.findByBoardId(boardId);
        return comments.stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getUser().getId(),
                        comment.getBoard().getId(),
                        comment.getIsReported(),
                        comment.getContent(),
                        comment.getCreateAt(), // 댓글 작성 시간 전달
                        TimeUtil.formatRelativeTime(comment.getCreateAt())

                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse createComment(Long boardId, Long userId, String commentContent) { // 댓글 작성 (예시: 댓글 내용은 요청 body로 받아야 함)
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        Comment comment = new Comment();
        comment.setBoard(board);
        comment.setUser(user);
        comment.setContent(commentContent);

        Comment savedComment = commentRepository.save(comment);
        return new CommentResponse(
                savedComment.getId(),
                savedComment.getUser().getId(),
                savedComment.getBoard().getId(),
                savedComment.getIsReported(),
                savedComment.getContent(),
                savedComment.getCreateAt(), // 댓글 작성 시간 전달
                TimeUtil.formatRelativeTime(savedComment.getCreateAt())
        );
    }
    @Transactional
    public void deleteComment(Long commentId, Long userId) { // 댓글 삭제 (본인 또는 관리자 권한 체크)
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        // 본인 댓글 또는 관리자 권한 체크 로직 (예시)
        if (!comment.getUser().getId().equals(userId) /* && !isAdmin(userId) */) {
            throw new CustomException(ErrorCode.UNAUTHORIZED); // 권한 없음 에러
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public CommentResponse updateComment (Long commentId, Long userId, String commentContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        if (!comment.getUser().getId().equals(userId) /* && !isAdmin(userId) */) {
            throw new CustomException(ErrorCode.UNAUTHORIZED); // 권한 없음 에러
        }

        comment.setContent(commentContent);
        Comment updatedComment = commentRepository.save(comment);

        return new CommentResponse(
                updatedComment.getId(),
                updatedComment.getUser().getId(),
                updatedComment.getBoard().getId(),
                updatedComment.getIsReported(),
                updatedComment.getContent(),
                updatedComment.getCreateAt(),
                TimeUtil.formatRelativeTime(updatedComment.getCreateAt())
        );

    }

}
