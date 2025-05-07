package com.newsvision.admin.controller;


import com.newsvision.admin.service.BoardListService;
import com.newsvision.board.dto.response.BoardResponse;
import com.newsvision.board.service.BoardService;
import com.newsvision.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/boards")
@RequiredArgsConstructor
@Tag(name = "커뮤니티 리스트 컨트롤러", description = "관리자용 커뮤니티 관리 API")
public class AdminBoardManageController {
    private final BoardService boardService;
    private final BoardListService boardListService;

    @Operation(
            summary = "관리자 게시글 목록 조회",
            description = "카테고리별 또는 전체 커뮤니티 게시글을 페이지네이션 형태로 조회합니다. 관리자 권한 필요.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BoardResponse>>> getBoardList(
            @RequestParam(required = false) Long categoryId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        Page<BoardResponse> boardList = boardService.getBoardsList(categoryId, pageable);
        return ResponseEntity.ok(ApiResponse.success(boardList));
    }

    @Operation(
            summary = "최신 인기 게시글 리스트 조회 (최대 수)",
            description = "관리자가 최신 인기 게시글을 전체 리스트로 가져옵니다. 관리자 권한 필요.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/min")
    public ResponseEntity<ApiResponse<List<BoardResponse>>> getBoards(
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        List<BoardResponse> boards = boardListService.getMaxBoardsList();
        return ResponseEntity.ok(ApiResponse.success(boards));
    }

    @Operation(
            summary = "게시글 삭제",
            description = "게시글 ID와 작성자 ID를 받아 해당 게시글을 삭제합니다. 관리자 권한 필요.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<ApiResponse<String>> deleteBoard(
            @PathVariable Long boardId,
            @RequestParam Long userId,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        boardService.deleteBoard(boardId, userId);
        return ResponseEntity.ok(ApiResponse.success("게시글(ID: " + boardId + ") 삭제 완료"));
    }
}
