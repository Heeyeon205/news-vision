package com.newsvision.board.controller;


import com.newsvision.board.entity.Board;
import com.newsvision.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

//    @GetMapping
//    public String boardPage(Model model, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
//        Page<Board> boardPage = boardService.getBoardsPage(pageable); // 메소드 이름 복수형으로 수정
//        model.addAttribute("boardPage", boardPage);
//        return "board/boardMain";
//    }
            @GetMapping // GET 요청을 처리하는 엔드포인트 (기본 경로 "/api/boards" + GET)
            public Page<Board> getBoards(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
                return boardService.getBoardsPage(pageable); // BoardService를 통해 페이징된 게시글 데이터를 가져와서 반환
}

}
