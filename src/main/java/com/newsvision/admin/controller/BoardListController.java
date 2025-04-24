package com.newsvision.admin.controller;

// ... (other imports)




import com.newsvision.admin.service.BoardListService;
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
    private final BoardListService boardListService;

    @GetMapping
   public ResponseEntity<List<BoardResponse>> getBoardList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId
            ){

        List<BoardResponse> boardList = boardService.getBoardsList(page, size, categoryId);
        return ResponseEntity.ok(boardList);
    }



    @GetMapping("/max")
    public ResponseEntity<List<BoardResponse>> getBoards() {
        List<BoardResponse> boards = boardListService.getMaxBoardsList(); // 파라미터 없이 호출
        return ResponseEntity.ok(boards);
    }



//    @GetMapping("/max")
//    public ResponseEntity<Page<BoardResponse>> getBoardList(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createAt").ascending());
//        Page<BoardResponse> boardList = boardListService.getBoardList(pageRequest);
//        return ResponseEntity.ok(boardList);
//    }


    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @PathVariable Long boardId,
            @RequestParam Long userId) {

        boardService.deleteBoard(boardId, userId);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }
}


