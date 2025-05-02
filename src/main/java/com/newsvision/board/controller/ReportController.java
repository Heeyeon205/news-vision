package com.newsvision.board.controller;


import com.newsvision.board.service.ReportService;
import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "ReportController", description = "신고 API")
public class ReportController {
    private final ReportService reportService;

    @Operation(summary = "게시글 신고", description = "게시글 신고")
    @PostMapping("/boards/{boardId}")//(게시글 신고)
    public ResponseEntity<ApiResponse<Void>> reportBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        Long userId = userDetails.getId();
        reportService.reportBoard(boardId, userId);
        return ResponseEntity.ok(ApiResponse.success());
    }
    @Operation(summary = "댓글 신고", description = "댓글 신고")
    @PostMapping("/comments/{commentId}") //(댓글 신고)
    public ResponseEntity<ApiResponse<Void>> reportComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        reportService.reportComment(commentId, userId);
        return ResponseEntity.ok(ApiResponse.success());
    }


}
