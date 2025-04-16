package com.newsvision.board.service;

import com.newsvision.board.entity.Board;
import com.newsvision.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;

    public Page<Board> getBoardsPage(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }



}
