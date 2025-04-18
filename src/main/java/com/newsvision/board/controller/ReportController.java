package com.newsvision.board.controller;


import com.newsvision.board.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Slf4j
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/boards/{boardId}")//(게시글 신고)
    public ResponseEntity<Void> reportBoard(@PathVariable Long boardId, @RequestParam Long userId) {
        reportService.reportBoard(boardId, userId);
        return new ResponseEntity<>(HttpStatus.CREATED); // 201 Created 상태 코드 반환 (신고 성공)
    }
    @PostMapping("/comments/{commentId}") //(댓글 신고)
    public ResponseEntity<Void> reportComment(@PathVariable Long commentId, @RequestParam Long userId) {
        reportService.reportComment(commentId, userId);
        return new ResponseEntity<>(HttpStatus.CREATED); // 201 Created 상태 코드 반환 (신고 성공)
    }


}
