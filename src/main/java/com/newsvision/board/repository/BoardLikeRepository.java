package com.newsvision.board.repository;

import com.newsvision.board.entity.Board;
import com.newsvision.board.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    boolean existsByBoardIdAndUserId(Long boardId, Long userId); // 특정 게시물과 유저의 좋아요 존재 여부 확인
    void deleteByBoardIdAndUserId(Long boardId, Long userId); // 특정 게시물과 유저의 좋아요 삭제
    int countByBoardId(Long boardId); // 특정 게시물의 좋아요 개수 Count
}
