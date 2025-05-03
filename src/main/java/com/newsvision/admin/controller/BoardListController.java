package com.newsvision.admin.controller;

import com.newsvision.admin.service.BoardListService;
import com.newsvision.board.service.BoardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/boards")
@RequiredArgsConstructor
@Tag(name = "BoradListController", description = "관리자용 커뮤니티 관리 API")
public class BoardListController {
//    private final BoardService boardService;
//    private final BoardListService boardListService;

//    @GetMapping
//    public ResponseEntity<ApiResponse<Page<BoardResponse>>> getBoardList(
//            @RequestParam(required = false) Long categoryId,
//            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
//            @AuthenticationPrincipal UserDetails userDetails) {
//
//        if (userDetails == null || !userDetails.getAuthorities().stream()
//                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
//            return ResponseEntity.status(403).body(null);
//        }
//
//        Page<BoardResponse> boardList = boardService.getBoardsList(categoryId,pageable);
//        return ResponseEntity.ok(ApiResponse.success(boardList));
//    }
//
//
//    @GetMapping("/min")
//    public ResponseEntity<ApiResponse<List<BoardResponse>>> getBoards(
//            @AuthenticationPrincipal UserDetails userDetails) {
//
//       if (userDetails == null || !userDetails.getAuthorities().stream()
//               .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
//          return ResponseEntity.status(403).body(null);
//       }
//
//        List<BoardResponse> boards = boardListService.getMaxBoardsList();
//        return ResponseEntity.ok(ApiResponse.success(boards));
//    }
//
//    @DeleteMapping("/delete/{boardId}")
//    public ResponseEntity<ApiResponse<String>> deleteBoard(
//            @PathVariable Long boardId,
//            @RequestParam Long userId,
//           @AuthenticationPrincipal UserDetails userDetails){
//        if (userDetails == null || !userDetails.getAuthorities().stream()
//              .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
//         return ResponseEntity.status(403).body(null);}
//
//
//        boardService.deleteBoard(boardId, userId);
//        return ResponseEntity.ok(ApiResponse.success("게시글(ID: " + boardId + ") 삭제 완료"));
//    }
}


