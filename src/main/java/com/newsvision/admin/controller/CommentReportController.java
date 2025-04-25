package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.CommentReportResponse;
import com.newsvision.admin.service.CommentReportService;
import com.newsvision.global.exception.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<CommentReportResponse>>> getCommentReports() {
        return ResponseEntity.ok(ApiResponse.success(commentReportService.getAllCommentReports()));
    }

    @GetMapping("/max")
    public ResponseEntity<ApiResponse<List<CommentReportResponse>>> getMaxCommentReports() {
        return ResponseEntity.ok(ApiResponse.success(commentReportService.getMaxAllCommentReports()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        commentReportService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/{reportId}/mark")
    public ResponseEntity<ApiResponse<String>> markCommentAsReported(@PathVariable Long reportId) {
        commentReportService.markCommentAsReported(reportId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}