package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.BoardReportResponse;
import com.newsvision.admin.service.BoardReportService;
import com.newsvision.global.exception.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/boardreports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // 개발 중 프론트 접근 허용
public class BoardReportController {

    private final BoardReportService boardReportService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BoardReportResponse>>> getAllReports() {
        return ResponseEntity.ok(ApiResponse.success(boardReportService.getAllReports()));
    }


    @GetMapping("/max")
    public ResponseEntity<ApiResponse<List<BoardReportResponse>>> getMaxAllReports() {
        return ResponseEntity.ok(ApiResponse.success(boardReportService.getMaxAllReports()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        boardReportService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/{reportId}/mark")
    public ResponseEntity<ApiResponse<String>> markBoardReported(@PathVariable Long reportId) {
        boardReportService.markBoardAsReported(reportId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<String> updateReport(
//            @PathVariable Long id,
//            @RequestParam(required = false) Long userId,
//            @RequestParam(required = false) Long boardId) {
//
//        boardReportService.updateBoardReport(id, userId, boardId);
//        return ResponseEntity.ok("신고 정보가 성공적으로 수정되었습니다.");
//    }
}