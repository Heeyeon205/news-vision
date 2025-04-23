package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.CommentReportResponse;
import com.newsvision.admin.service.CommentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/commentreports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CommentReportController {

    private final CommentReportService commentReportService;

    @GetMapping
    public ResponseEntity<List<CommentReportResponse>> getCommentReports() {
        return ResponseEntity.ok(commentReportService.getAllCommentReports());
    }

    @GetMapping("/max")
    public ResponseEntity<List<CommentReportResponse>> getMaxCommentReports() {
        return ResponseEntity.ok(commentReportService.getMaxAllCommentReports());
    }

    // 카테고리 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        commentReportService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{reportId}/mark")
    public ResponseEntity<String> markCommentAsReported(@PathVariable Long reportId) {
        commentReportService.markCommentAsReported(reportId);
        return ResponseEntity.ok("댓글의 신고 상태가 true로 변경되었습니다.");
    }
}