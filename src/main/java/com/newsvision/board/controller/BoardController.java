package com.newsvision.board.controller;


import com.newsvision.board.controller.request.BoardCreateRequest;
import com.newsvision.board.controller.request.BoardUpdateRequest;
import com.newsvision.board.controller.response.BoardDetailResponse;
import com.newsvision.board.controller.response.BoardResponse;
import com.newsvision.board.entity.Board;
import com.newsvision.board.service.BoardService;
import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @GetMapping // GET 요청을 처리하는 엔드포인트 (기본 경로 "/api/boards" + GET)
    public ResponseEntity<ApiResponse<List<BoardResponse>>> getBoards(
            @RequestParam(defaultValue = "0") int page, // 페이지 번호 파라미터, 기본값 0
            @RequestParam(defaultValue = "10") int size, // 페이지 크기 파라미터, 기본값 10
            @RequestParam(required = false) Long categoryId
    ) {
        List<BoardResponse> boards = boardService.getBoardsList(page, size , categoryId);
        return ResponseEntity.ok(ApiResponse.success(boards)); // 200 OK 상태 코드와 함께 게시글 목록 반환
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardDetailResponse>> getBoardDetail(@PathVariable Long boardId) {
        Board board = boardService.findById(boardId);
        BoardDetailResponse boardDetail = boardService.getBoardDetail(board);
        return ResponseEntity.ok(ApiResponse.success(boardDetail));
    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // 게시글 작성 API
    public ResponseEntity<ApiResponse<BoardDetailResponse>> createBoard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "content")  String content,
            @RequestParam(value = "categoryId") Long categoryId
    ) {
        Long userId = userDetails.getId();
        BoardDetailResponse createdBoard = boardService.createBoard(userId,image,content,categoryId);
        return ResponseEntity.ok(ApiResponse.success(createdBoard));
    }
    @PutMapping(value = "/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // 게시글 수정 API
    public ResponseEntity<ApiResponse<BoardDetailResponse>> updateBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "categoryId") Long categoryId
    ) {
        Long userId = userDetails.getId();
        Board board = boardService.findById(boardId);
        BoardDetailResponse updatedBoard = boardService.updateBoard(board, userId,image, content,categoryId);
        return ResponseEntity.ok(ApiResponse.success(updatedBoard));
    }

    @DeleteMapping("/{boardId}") // 게시글 삭제 API
    public ResponseEntity<ApiResponse<Void>> deleteBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        boardService.deleteBoard(boardId, userId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/{boardId}/likes")
    public ResponseEntity<ApiResponse<Void>> likeBoard(@PathVariable Long boardId,@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        Board board = boardService.findById(boardId);
        log.info("BoardController.likeBoard 메서드 호출됨! boardId: {}, userId: {}", boardId, userId);

        boardService.likeBoard(board, userId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/{boardId}/views")
    public ResponseEntity<ApiResponse<Void>> incrementViewCount(@PathVariable Long boardId) {
        Board board = boardService.findById(boardId);
        boardService.incrementViewCount(board);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
