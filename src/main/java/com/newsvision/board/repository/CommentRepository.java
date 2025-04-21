package com.newsvision.board.repository;

import com.newsvision.board.entity.Board;
import com.newsvision.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardId(Long boardId);
    int countByBoardId(Long id);
}
