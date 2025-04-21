package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.BoardReportResponse;
import com.newsvision.admin.service.BoardReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/boardreports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // 개발 중 프론트 접근 허용
public class BoardReportController {

    private final BoardReportService boardReportService;

    @GetMapping
    public ResponseEntity<List<BoardReportResponse>> getAllReports() {
        return ResponseEntity.ok(boardReportService.getAllReports());
    }
}