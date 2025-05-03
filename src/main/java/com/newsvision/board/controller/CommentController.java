package com.newsvision.board.controller;

import com.newsvision.board.dto.request.CommentCreateRequest;
import com.newsvision.board.dto.request.CommentUpdateRequest;
import com.newsvision.board.dto.response.CommentResponse;
import com.newsvision.board.service.CommentService;
import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.poll.service.BoardCommentManager;
import com.newsvision.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name= "CommentController", description = "댓글 API")
public class CommentController {
    private final CommentService commentService;
    private final BoardCommentManager boardCommentManager;

    @Operation(summary = "특정 게시글 댓글 조회", description = "특정 게시글 댓글 조회")
    @GetMapping("/boards/{boardId}")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByBoardId(@PathVariable Long boardId) {
        List<CommentResponse> comments = commentService.getCommentsByBoardId(boardId);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }

    @Operation(summary = "댓글 작성", description = "댓글 작성")
    @PostMapping("/boards/{boardId}")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CommentCreateRequest request
    ) {
        Long userId = userDetails.getId();
        CommentResponse comment = boardCommentManager.createComment(boardId, userId, request.getContent()); // request에서 댓글 내용 추출하여 Service에 전달
        return ResponseEntity.ok(ApiResponse.success(comment));
    }

    @Operation(summary = "댓글 삭제", description = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        boardCommentManager.deleteComment(commentId, userId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "댓글 수정", description = "댓글 수정")
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CommentUpdateRequest request
    ) {
        Long userId = userDetails.getId();
        CommentResponse updatedComment = commentService.updateComment(commentId, userId, request.getContent()); // Service의 updateComment 메서드 호출
        return ResponseEntity.ok(ApiResponse.success(updatedComment));
    }
}
