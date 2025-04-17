package com.newsvision.board.controller;


import com.newsvision.board.controller.response.BoardResponse;
import com.newsvision.board.entity.Board;
import com.newsvision.board.service.BoardService;
import lombok.RequiredArgsConstructor;
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
public class BoardController {
    private final BoardService boardService;

    @GetMapping // GET 요청을 처리하는 엔드포인트 (기본 경로 "/api/boards" + GET)
    public ResponseEntity<List<BoardResponse>> getBoards(
            @RequestParam(defaultValue = "0") int page, // 페이지 번호 파라미터, 기본값 0
            @RequestParam(defaultValue = "10") int size) { // 페이지 크기 파라미터, 기본값 10
        List<BoardResponse> boards = boardService.getBoardsList(page, size);
        return new ResponseEntity<>(boards, HttpStatus.OK); // 200 OK 상태 코드와 함께 게시글 목록 반환
    }
    @PostMapping("/{boardId}/likes")
    public ResponseEntity<Void> likeBoard(@PathVariable Long boardId, @RequestParam Long userId) {
        boardService.likeBoard(boardId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{boardId}/views")
    public ResponseEntity<Void> incrementViewCount(@PathVariable Long boardId) {
        boardService.incrementViewCount(boardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
