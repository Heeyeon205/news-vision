package com.newsvision.board.repository;

import com.newsvision.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board,Long> {
    Page<Board> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Board> findByUserIdOrderByCreateAtDesc(Long userId, Pageable pageable);

    @Modifying
    @Query("UPDATE Board b SET b.category.id = :defaultId WHERE b.category.id = :categoryId")
    void updateCategoryIdToDefault(@Param("categoryId") Long categoryId, @Param("defaultId") Long defaultId);
}
