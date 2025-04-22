package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.CommentReportResponse;
import com.newsvision.admin.service.CommentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}