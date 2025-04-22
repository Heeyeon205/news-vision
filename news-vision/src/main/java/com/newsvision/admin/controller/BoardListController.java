package com.newsvision.admin.controller;

// ... (other imports)


import com.newsvision.board.controller.response.BoardResponse;
import com.newsvision.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/boards")
@RequiredArgsConstructor
public class BoardListController {
    private final BoardService boardService;


    @GetMapping
   public ResponseEntity<List<BoardResponse>> getBoardList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId
            ){

        List<BoardResponse> boardList = boardService.getBoardsList(page, size, categoryId);
        return ResponseEntity.ok(boardList);
    }
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @PathVariable Long boardId,
            @RequestParam Long userId) {

        boardService.deleteBoard(boardId, userId);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }
}


