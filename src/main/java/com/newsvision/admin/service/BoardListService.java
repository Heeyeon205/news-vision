package com.newsvision.admin.service;


import com.newsvision.board.dto.response.BoardResponse;
import com.newsvision.board.entity.Board;
import com.newsvision.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardListService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardListService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<BoardResponse> getMaxBoardsList() {
        int page = 0;
        int size = 10;

        //Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")); 최대
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")); //최저
        Page<Board> boardPage = boardRepository.findAll(pageable);

        return boardPage.getContent().stream().map(board -> {
            int likeCount = board.getBoardLikes() != null ? board.getBoardLikes().size() : 0;
            int commentCount = board.getComments() != null ? board.getComments().size() : 0;
            return new BoardResponse(board, likeCount, commentCount);
        }).collect(Collectors.toList());
    }

}