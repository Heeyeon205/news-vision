package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.BoardReportResponse;
import com.newsvision.admin.service.BoardReportService;
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
    public ResponseEntity<List<BoardReportResponse>> getAllReports() {
        return ResponseEntity.ok(boardReportService.getAllReports());
    }
    @GetMapping("/max")
    public ResponseEntity<List<BoardReportResponse>> getMaxAllReports() {
        return ResponseEntity.ok(boardReportService.getMaxAllReports());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        boardReportService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{reportId}/mark")
    public ResponseEntity<String> markBoardReported(@PathVariable Long reportId) {
        boardReportService.markBoardAsReported(reportId);
        return ResponseEntity.ok("해당 게시글의 신고 상태가 true로 변경되었습니다.");
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