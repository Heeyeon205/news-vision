package com.newsvision.board.service;

import com.newsvision.admin.controller.response.UserListResponse;
import com.newsvision.board.controller.response.BoardDetailResponse;
import com.newsvision.board.controller.response.BoardResponse;
import com.newsvision.board.entity.Board;
import com.newsvision.board.entity.BoardLike;
import com.newsvision.board.repository.BoardLikeRepository;
import com.newsvision.board.repository.BoardRepository;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final UserRepository userRepository;

    public List<BoardResponse> getBoardsList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        List<Board> boards = boardRepository.findAll(pageable).getContent();
        return boards.stream().map(board -> {
            long likeCount = (long) board.getBoardLikes().size(); // 좋아요 수 계산
            long commentCount = (long) board.getComments().size(); // 댓글 수 계산
            return new BoardResponse(
                    board.getTitle(),
                    board.getContent(),
                    board.getCategory().getId(),
                    board.getCreateAt(),
                    board.getUser().getId(),
                    board.getImage(),
                    board.getView(),
                    board.getNewsId(),
                    board.getIsReported(),
                    likeCount,
                    commentCount
            );
        }).collect(Collectors.toList());
    }

    public BoardDetailResponse getBoardDetail(Long boardId) { // 게시글 상세 조회
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        long likeCount = (long) board.getBoardLikes().size(); // 좋아요 수 계산
        long commentCount = (long) board.getComments().size(); // 댓글 수 계산
        return new BoardDetailResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getCategory().getId(),
                board.getCreateAt(),
                board.getUser().getId(),
                board.getImage(),
                board.getView(),
                board.getNewsId(),
                board.getIsReported(),
                likeCount,
                commentCount
        );
    }



    @Transactional
    public void likeBoard(Long boardId, Long userId) { // 좋아요 기능
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        User user = userRepository.findById(userId) // userId로 User 엔티티 조회 (실제 환경에서는 인증 정보에서 userId를 얻어야 함)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        boolean alreadyLiked = boardLikeRepository.existsByBoardIdAndUserId(boardId, userId);

        if (alreadyLiked) { // 이미 좋아요를 눌렀으면 좋아요 취소
            boardLikeRepository.deleteByBoardIdAndUserId(boardId, userId);
        } else { // 좋아요를 누르지 않았으면 좋아요 추가
            BoardLike boardLike = new BoardLike();
            boardLike.setBoard(board);
            boardLike.setUser(user);
            boardLikeRepository.save(boardLike);
        }
    }
    @Transactional
    public void incrementViewCount(Long boardId) { // 조회수 증가 기능
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        board.setView(board.getView() + 1);
        boardRepository.save(board);
    }



}




