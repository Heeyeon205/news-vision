package com.newsvision.board.controller;


import com.newsvision.board.dto.response.*;
import com.newsvision.board.entity.Board;
import com.newsvision.board.entity.BoardLike;
import com.newsvision.board.service.BoardLikeService;
import com.newsvision.board.service.BoardService;
import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "BoardController", description = "커뮤니티 API")
public class BoardController {
    private final BoardService boardService;
    private final UserService userService;
    private final BoardLikeService boardLikeService;

    @Operation(summary = "커뮤니티 글 불러오기", description = "커뮤니티 글 불러오기")
    @GetMapping // GET 요청을 처리하는 엔드포인트 (기본 경로 "/api/boards" + GET)
    public ResponseEntity<ApiResponse<List<BoardResponse>>> getBoards(
            @RequestParam(defaultValue = "0") int page, // 페이지 번호 파라미터, 기본값 0
            @RequestParam(defaultValue = "10") int size, // 페이지 크기 파라미터, 기본값 10
            @RequestParam(required = false) Long categoryId
    ) {
        List<BoardResponse> boards = boardService.getBoardsList(page, size , categoryId);
        return ResponseEntity.ok(ApiResponse.success(boards)); // 200 OK 상태 코드와 함께 게시글 목록 반환
    }

    @Operation(summary = "커뮤니티 글 상세보기", description = "커뮤니티 글 상세보기")
    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardDetailResponse>> getBoardDetail(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails != null ? userDetails.getId() : null;
        Board board = boardService.findById(boardId);
        boardService.incrementViewCount(board);
        BoardDetailResponse boardDetail = boardService.getBoardDetail(board, userId);
        return ResponseEntity.ok(ApiResponse.success(boardDetail));
    }

    @Operation(summary = "커뮤니티 글 작성", description = "커뮤니티 글 작성")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // 게시글 작성 API
    public ResponseEntity<ApiResponse<Long>> createBoard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "content")  String content,
            @RequestParam(value = "categoryId") Long categoryId
    ) {
        Long userId = userDetails.getId();
        BoardCreateResponse createdBoard = boardService.createBoard(userId, image, content, categoryId);
        return ResponseEntity.ok(ApiResponse.success(createdBoard.getId()));
    }

    @Operation(summary = "커뮤니티 글 수정 폼", description = "커뮤니티 글 수정 폼")
    @GetMapping("/update/{boardId}") // 유저가 수정하려면 해당 글쓴 유저인지
    public ResponseEntity<ApiResponse<BoardUpdateResponse>> updateBoard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long boardId
    ){
        Board board = boardService.findById(boardId);
        userService.matchUserId(userDetails.getId(),board.getUser().getId());
        BoardUpdateResponse response = boardService.getBoardUpdate(board);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "커뮤니티 글 수정", description = "커뮤니티 글 수정")
    @PutMapping(value = "/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BoardUpdateResponse>> updateBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "categoryId") Long categoryId
    ) {
        Long userId = userDetails.getId();
        Board board = boardService.findById(boardId);
        BoardUpdateResponse updatedBoard = boardService.updateBoard(board, userId,image, content,categoryId);
        return ResponseEntity.ok(ApiResponse.success(updatedBoard));
    }

    @Operation(summary = "커뮤니티 글 삭제", description = "커뮤니티 글 삭제")
    @DeleteMapping("/{boardId}") // 게시글 삭제 API
    public ResponseEntity<ApiResponse<Void>> deleteBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        boardService.deleteBoard(boardId, userId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "커뮤니티 글 좋아요 추가", description = "커뮤니티 글 좋아요 추가")
    @PostMapping("/{boardId}/like")
    public ResponseEntity<ApiResponse<BoardLikeResponse>> addLike(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        boardService.addLike(boardId, userDetails.getId());
        int LikeCount = boardLikeService.countByBoardId(boardId);
        Boolean isLike = true;
        return ResponseEntity.ok(ApiResponse.success(new BoardLikeResponse(LikeCount, isLike)));
    }

    @Operation(summary = "커뮤니티 글 좋아요 삭제", description = "커뮤니티 글 좋아요 삭제")
    @DeleteMapping("/{boardId}/like")
    public ResponseEntity<ApiResponse<BoardLikeResponse>> removeLike(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        boardService.removeLike(boardId, userDetails.getId());
        int LikeCount = boardLikeService.countByBoardId(boardId);
        Boolean isLike = false;
        return ResponseEntity.ok(ApiResponse.success(new BoardLikeResponse(LikeCount, isLike)));
    }
}