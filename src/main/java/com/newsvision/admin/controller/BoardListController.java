package com.newsvision.admin.controller;

// ... (other imports)




import com.newsvision.admin.service.BoardListService;
import com.newsvision.board.controller.response.BoardResponse;
import com.newsvision.board.service.BoardService;
import com.newsvision.global.exception.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<ApiResponse<List<BoardResponse>>> getBoardList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        List<BoardResponse> boardList = boardService.getBoardsList(page, size, categoryId);
        return ResponseEntity.ok(ApiResponse.success(boardList));
    }


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



    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<ApiResponse<String>> deleteBoard(
            @PathVariable Long boardId,
            @RequestParam Long userId,
           @AuthenticationPrincipal UserDetails userDetails){
        if (userDetails == null || !userDetails.getAuthorities().stream()
              .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
         return ResponseEntity.status(403).body(null);}


        boardService.deleteBoard(boardId, userId);
        return ResponseEntity.ok(ApiResponse.success("게시글(ID: " + boardId + ") 삭제 완료"));
    }
}


