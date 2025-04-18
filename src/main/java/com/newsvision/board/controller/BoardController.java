package com.newsvision.board.controller;


import com.newsvision.board.controller.request.BoardCreateRequest;
import com.newsvision.board.controller.request.BoardUpdateRequest;
import com.newsvision.board.controller.response.BoardDetailResponse;
import com.newsvision.board.controller.response.BoardResponse;
import com.newsvision.board.entity.Board;
import com.newsvision.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @GetMapping // GET 요청을 처리하는 엔드포인트 (기본 경로 "/api/boards" + GET)
    public ResponseEntity<List<BoardResponse>> getBoards(
            @RequestParam(defaultValue = "0") int page, // 페이지 번호 파라미터, 기본값 0
            @RequestParam(defaultValue = "10") int size) { // 페이지 크기 파라미터, 기본값 10
        List<BoardResponse> boards = boardService.getBoardsList(page, size);
        return new ResponseEntity<>(boards, HttpStatus.OK); // 200 OK 상태 코드와 함께 게시글 목록 반환
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDetailResponse> getBoardDetail(@PathVariable Long boardId) {
        BoardDetailResponse boardDetail = boardService.getBoardDetail(boardId);
        return new ResponseEntity<>(boardDetail, HttpStatus.OK);
    }
    @PostMapping // 게시글 작성 API
    public ResponseEntity<BoardDetailResponse> createBoard(
            @RequestParam Long userId,
            @RequestBody BoardCreateRequest request // 요청 body로 게시글 정보 받기
    ) {
        BoardDetailResponse createdBoard = boardService.createBoard(userId, request);
        return new ResponseEntity<>(createdBoard, HttpStatus.CREATED);
    }
    @PutMapping("/{boardId}") // 게시글 수정 API
    public ResponseEntity<BoardDetailResponse> updateBoard(
            @PathVariable Long boardId,
            @RequestParam Long userId,
            @RequestBody BoardUpdateRequest request // 요청 body로 수정할 게시글 정보 받기
    ) {
        BoardDetailResponse updatedBoard = boardService.updateBoard(boardId, userId, request);
        return new ResponseEntity<>(updatedBoard, HttpStatus.OK);
    }

    @DeleteMapping("/{boardId}") // 게시글 삭제 API
    public ResponseEntity<Void> deleteBoard(
            @PathVariable Long boardId,
            @RequestParam Long userId
    ) {
        boardService.deleteBoard(boardId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{boardId}/likes")
    public ResponseEntity<Void> likeBoard(@PathVariable Long boardId, @RequestParam Long userId) {
        log.info("BoardController.likeBoard 메서드 호출됨! boardId: {}, userId: {}", boardId, userId);
        boardService.likeBoard(boardId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{boardId}/views")
    public ResponseEntity<Void> incrementViewCount(@PathVariable Long boardId) {
        boardService.incrementViewCount(boardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
