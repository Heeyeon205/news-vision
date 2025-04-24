package com.newsvision.admin.service;


import com.newsvision.board.controller.response.BoardResponse;
import com.newsvision.board.repository.BoardRepository;
import com.newsvision.global.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BoardListService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardListService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }


    public Page<BoardResponse> getBoardList(Pageable pageable) {
        // 임시 값 지원이가 만들렴 ,....
        int likeCount = 1;
        int commentCount = 1;
        return boardRepository.findAll(pageable)
                .map(board -> new BoardResponse(board, likeCount, commentCount));
    }


    private String calculateRelativeTime(java.time.LocalDateTime createdAt) {
        // Implement your relative time calculation logic here
        // This is a simple example
        java.time.Duration duration = java.time.Duration.between(createdAt, java.time.LocalDateTime.now());
        long minutes = duration.toMinutes();

        if (minutes < 60) {
            return minutes + " minutes ago";
        } else if (minutes < 1440) {
            return (minutes / 60) + " hours ago";
        } else {
            return (minutes / 1440) + " days ago";
        }
    }
}