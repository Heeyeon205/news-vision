package com.newsvision.board.controller;

import com.newsvision.board.dto.request.CommentCreateRequest;
import com.newsvision.board.dto.request.CommentUpdateRequest;
import com.newsvision.board.dto.response.CommentResponse;
import com.newsvision.board.service.CommentService;
import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;

    @GetMapping("/boards/{boardId}") //(특정 게시글 댓글 목록 조회)
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByBoardId(@PathVariable Long boardId) {
        List<CommentResponse> comments = commentService.getCommentsByBoardId(boardId);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }

    @PostMapping("/boards/{boardId}") // (댓글 작성)
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CommentCreateRequest request // @RequestBody 로 CommentCreateRequest 받기
    ) {
        Long userId = userDetails.getId();
        CommentResponse comment = commentService.createComment(boardId, userId, request.getContent()); // request에서 댓글 내용 추출하여 Service에 전달
        return ResponseEntity.ok(ApiResponse.success(comment));
    }

    @DeleteMapping("/{commentId}") // (댓글 삭제)
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PutMapping("/{commentId}") // (댓글 수정)
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CommentUpdateRequest request
    ) {
        Long userId = userDetails.getId();
        CommentResponse updatedComment = commentService.updateComment(commentId, userId, request.getContent()); // Service의 updateComment 메서드 호출
        return ResponseEntity.ok(ApiResponse.success(updatedComment)); // 수정 완료 200 OK 상태 코드 반환
    }
}
