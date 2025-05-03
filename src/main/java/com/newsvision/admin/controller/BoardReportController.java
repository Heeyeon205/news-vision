package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.BoardReportResponse;
import com.newsvision.admin.service.BoardReportService;
import com.newsvision.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/boardreports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "게시글 신고 관리 컨트롤러", description = "커뮤니티 게시글 신고 관리 API")
public class BoardReportController {

    private final BoardReportService boardReportService;

    @Operation(
            summary = "전체 게시글 신고 목록 조회",
            description = "모든 게시글 신고 내역을 리스트 형태로 반환합니다. 관리자 권한 필요.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<BoardReportResponse>>> getAllReports(
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        return ResponseEntity.ok(ApiResponse.success(boardReportService.getAllReports()));
    }

    @Operation(
            summary = "게시글 신고 삭제",
            description = "특정 ID의 게시글 신고 항목을 삭제합니다. 관리자 권한 필요.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        boardReportService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(id + " 삭제 완료"));
    }

    @Operation(
            summary = "게시글 신고 처리",
            description = "해당 게시글 신고 건을 '신고 처리 완료' 상태로 변경합니다. 관리자 권한 필요.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/{reportId}/mark")
    public ResponseEntity<ApiResponse<String>> markBoardReported(
            @PathVariable Long reportId,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        boardReportService.markBoardAsReported(reportId);
        return ResponseEntity.ok(ApiResponse.success(reportId + " 신고 처리 완료"));
    }
}
