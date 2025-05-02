package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.CommentReportResponse;
import com.newsvision.admin.service.CommentReportService;
import com.newsvision.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/commentreports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "CommentReportController", description = "댓글 신고 관리 API")
public class CommentReportController {

    private final CommentReportService commentReportService;

    @Operation(summary = "댓글 신고 모아보기", description = "댓글 신고 모아보기")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentReportResponse>>> getCommentReports(
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        return ResponseEntity.ok(ApiResponse.success(commentReportService.getAllCommentReports()));
    }

//    @GetMapping("/max")
//    public ResponseEntity<ApiResponse<List<CommentReportResponse>>> getMaxCommentReports(
//            @AuthenticationPrincipal UserDetails userDetails) {
//
//        if (userDetails == null || !userDetails.getAuthorities().stream()
//                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
//            return ResponseEntity.status(403).body(null);
//        }
//
//        return ResponseEntity.ok(ApiResponse.success(commentReportService.getMaxAllCommentReports()));
//    }

    @Operation(summary = "댓글 신고 삭제", description = "댓글 신고 삭제")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        commentReportService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("댓글 신고 카테고리(ID: " + id + ") 삭제 완료"));
    }

    @Operation(summary = "댓글 신고 처리", description = "댓글 신고 처리")
    @PutMapping("/{reportId}/mark")
    public ResponseEntity<ApiResponse<String>> markCommentAsReported(
            @PathVariable Long reportId,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        commentReportService.markCommentAsReported(reportId);
        return ResponseEntity.ok(ApiResponse.success("댓글 신고(ID: " + reportId + ")가 true로 설정되었습니다."));
    }}