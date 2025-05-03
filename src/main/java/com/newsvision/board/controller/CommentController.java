package com.newsvision.board.controller;

import com.newsvision.board.dto.request.CommentCreateRequest;
import com.newsvision.board.dto.request.CommentUpdateRequest;
import com.newsvision.board.dto.response.CommentResponse;
import com.newsvision.board.service.CommentService;
import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@Tag(name = "댓글 컨트롤러", description = "댓글 API")
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;

    @Operation(summary = "특정 게시글 댓글 목록 조회", description = "게시글 ID에 해당하는 모든 댓글을 조회합니다.")
    @GetMapping("/boards/{boardId}")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByBoardId(@PathVariable Long boardId) {
        List<CommentResponse> comments = commentService.getCommentsByBoardId(boardId);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }

    @Operation(summary = "댓글 작성", description = "특정 게시글에 댓글을 작성합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/boards/{boardId}")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CommentCreateRequest request
    ) {
        Long userId = userDetails.getId();
        CommentResponse comment = commentService.createComment(boardId, userId, request.getContent());
        return ResponseEntity.ok(ApiResponse.success(comment));
    }

    @Operation(summary = "댓글 삭제", description = "자신이 작성한 댓글을 삭제합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "댓글 수정", description = "자신이 작성한 댓글의 내용을 수정합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CommentUpdateRequest request
    ) {
        Long userId = userDetails.getId();
        CommentResponse updatedComment = commentService.updateComment(commentId, userId, request.getContent());
        return ResponseEntity.ok(ApiResponse.success(updatedComment));
    }
}
