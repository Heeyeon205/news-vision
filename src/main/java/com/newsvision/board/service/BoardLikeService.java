package com.newsvision.board.service;


import com.newsvision.board.entity.Board;
import com.newsvision.board.entity.BoardLike;
import com.newsvision.board.repository.BoardLikeRepository;
import com.newsvision.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardLikeService {
    private final BoardLikeRepository boardLikeRepository;

    public Boolean existsByBoardIdAndUserId(Long boardId, Long userId) {
        return boardLikeRepository.existsByBoardIdAndUserId(boardId, userId);
    }

    public int countByBoardId(Long boardId) {
        return boardLikeRepository.countByBoardId(boardId);
    }

    @Transactional
    public void save(Board board, User user) {
       BoardLike boardLike = BoardLike.builder()
               .board(board)
               .user(user)
               .build();
       boardLikeRepository.save(boardLike);
    }

    @Transactional
    public void delete(Long boardId, Long userId) {
        boardLikeRepository.deleteByBoardIdAndUserId(boardId, userId);
    }
}


