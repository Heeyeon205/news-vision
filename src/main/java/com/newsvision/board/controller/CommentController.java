package com.newsvision.board.controller;

import com.newsvision.board.controller.request.CommentCreateRequest;
import com.newsvision.board.controller.request.CommentUpdateRequest;
import com.newsvision.board.controller.response.CommentResponse;
import com.newsvision.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/boards/{boardId}") //(특정 게시글 댓글 목록 조회)
    public ResponseEntity<List<CommentResponse>> getCommentsByBoardId(@PathVariable Long boardId) {
        List<CommentResponse> comments = commentService.getCommentsByBoardId(boardId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping("/boards/{boardId}") // (댓글 작성)
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long boardId,
            @RequestParam Long userId,
            @RequestBody CommentCreateRequest request // @RequestBody 로 CommentCreateRequest 받기
    ) {
        CommentResponse comment = commentService.createComment(boardId, userId, request.getContent()); // request에서 댓글 내용 추출하여 Service에 전달
        return new ResponseEntity<>(comment, HttpStatus.CREATED); // 201 Created 상태 코드 반환
    }

    @DeleteMapping("/{commentId}") // (댓글 삭제)
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @RequestParam Long userId) {
        commentService.deleteComment(commentId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/{commentId}") // (댓글 수정)
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestParam Long userId,
            @RequestBody CommentUpdateRequest request
    ) {
        CommentResponse updatedComment = commentService.updateComment(commentId, userId, request.getContent()); // Service의 updateComment 메서드 호출
        return new ResponseEntity<>(updatedComment, HttpStatus.OK); // 수정 완료 200 OK 상태 코드 반환
    }

}
