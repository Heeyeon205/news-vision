package com.newsvision.admin.controller;

// ... (other imports)

import com.newsvision.board.controller.response.BoardResponse;
import com.newsvision.board.entity.Board;
import com.newsvision.board.repository.BoardRepository;
import com.newsvision.board.service.BoardService;
import com.newsvision.category.entity.Categories;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import com.p6spy.engine.logging.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;




@RestController
@RequestMapping("/admin/boards")
@RequiredArgsConstructor // Marks this class as a Spring service component
public class BoardListController {
    private final BoardService boardService;


    @GetMapping
    public ResponseEntity<List<BoardResponse>> getBoardList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        List<BoardResponse> boardList = boardService.getBoardsList(page, size);
        return ResponseEntity.ok(boardList);
    }

}



