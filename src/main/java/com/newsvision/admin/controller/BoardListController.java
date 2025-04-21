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
import kotlin.io.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/boards")
@RequiredArgsConstructor
public class BoardListController {
    private final BoardService boardService;

    private static final Logger log = LoggerFactory.getLogger(BoardListController.class);

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
    public ResponseEntity<?> deleteBoard(
            @PathVariable Long boardId,
            @RequestParam Long userId
    ) {
        log.info("[DELETE /admin/boards/{}] Request received. User ID from param: {}", boardId, userId);

        try {

            boardService.deleteBoard(boardId, userId);

            log.info("[DELETE /admin/boards/{}] Board deleted successfully. User ID from param: {}", boardId, userId);
            return ResponseEntity.noContent().build();


        } catch (CustomException e) {
            log.warn("[DELETE /admin/boards/{}] Failed due to CustomException. User ID: {}. Reason: {}", boardId, userId, e.getMessage());
            HttpStatus status = HttpStatus.BAD_REQUEST; // 기본값
            if (e.getErrorCode() == ErrorCode.NOT_FOUND) {

                status = HttpStatus.NOT_FOUND; // 404
            } else if (e.getErrorCode() == ErrorCode.UNAUTHORIZED) {

                status = HttpStatus.FORBIDDEN; // 403
            }
            return ResponseEntity.status(status)
                    .body(Map.of("success", false, "message", e.getMessage()));

        } catch (DataIntegrityViolationException e) {

            log.error("[DELETE /admin/boards/{}] Data integrity violation. User ID: {}", boardId, userId, e);
            return ResponseEntity.status(HttpStatus.CONFLICT) // 409 Conflict
                    .body(Map.of("success", false, "message", "게시글을 삭제할 수 없습니다. 다른 데이터에서 참조하고 있습니다."));

        } catch (Exception e) {

            log.error("[DELETE /admin/boards/{}] Unexpected error occurred. User ID: {}", boardId, userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
                    .body(Map.of("success", false, "message", "게시글 삭제 중 예상치 못한 서버 오류가 발생했습니다."));
        }
    }
}