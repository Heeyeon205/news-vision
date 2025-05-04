package com.newsvision.board.controller;

import com.newsvision.board.dto.response.*;
import com.newsvision.board.entity.Board;
import com.newsvision.board.service.BoardLikeService;
import com.newsvision.board.service.BoardService;
import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
@Tag(name = "커뮤니티 컨트롤러", description = "커뮤니티 API")
public class BoardController {
    private final BoardService boardService;
    private final UserService userService;
    private final BoardLikeService boardLikeService;

    @Operation(summary = "커뮤니티 글 목록 조회", description = "카테고리별 또는 전체 커뮤니티 글을 페이징 처리하여 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BoardResponse>>> getBoards(
            @RequestParam(required = false) Long categoryId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<BoardResponse> boards = boardService.getBoardsList(categoryId, pageable);
        return ResponseEntity.ok(ApiResponse.success(boards));
    }

    @Operation(summary = "커뮤니티 글 상세보기", description = "특정 커뮤니티 글의 상세 정보를 조회합니다.", security = @SecurityRequirement(name = "bearerAuth"))
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

    @Operation(summary = "커뮤니티 글 작성", description = "새로운 커뮤니티 글을 작성합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Long>> createBoard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "categoryId") Long categoryId
    ) {
        Long userId = userDetails.getId();
        BoardCreateResponse createdBoard = boardService.createBoard(userId, image, content, categoryId);
        return ResponseEntity.ok(ApiResponse.success(createdBoard.getId()));
    }

    @Operation(summary = "커뮤니티 글 수정 폼 조회", description = "수정을 위한 글의 기본 정보를 조회합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/update/{boardId}")
    public ResponseEntity<ApiResponse<BoardUpdateResponse>> updateBoard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long boardId
    ) {
        Board board = boardService.findById(boardId);
        userService.matchUserId(userDetails.getId(), board.getUser().getId());
        BoardUpdateResponse response = boardService.getBoardUpdate(board);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "커뮤니티 글 수정", description = "커뮤니티 글을 수정합니다.", security = @SecurityRequirement(name = "bearerAuth"))
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
        BoardUpdateResponse updatedBoard = boardService.updateBoard(board, userId, image, content, categoryId);
        return ResponseEntity.ok(ApiResponse.success(updatedBoard));
    }

    @Operation(summary = "커뮤니티 글 삭제", description = "커뮤니티 글을 삭제합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        boardService.deleteBoard(boardId, userId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "커뮤니티 글 좋아요 추가", description = "커뮤니티 글에 좋아요를 추가합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/{boardId}/like")
    public ResponseEntity<ApiResponse<BoardLikeResponse>> addLike(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        boardService.addLike(boardId, userDetails.getId());
        int likeCount = boardLikeService.countByBoardId(boardId);
        return ResponseEntity.ok(ApiResponse.success(new BoardLikeResponse(likeCount, true)));
    }

    @Operation(summary = "커뮤니티 글 좋아요 취소", description = "커뮤니티 글의 좋아요를 취소합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{boardId}/like")
    public ResponseEntity<ApiResponse<BoardLikeResponse>> removeLike(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        boardService.removeLike(boardId, userDetails.getId());
        int likeCount = boardLikeService.countByBoardId(boardId);
        return ResponseEntity.ok(ApiResponse.success(new BoardLikeResponse(likeCount, false)));
    }
}
