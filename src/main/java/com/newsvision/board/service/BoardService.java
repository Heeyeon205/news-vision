package com.newsvision.board.service;

import com.newsvision.board.controller.request.BoardCreateRequest;
import com.newsvision.board.controller.request.BoardUpdateRequest;
import com.newsvision.board.controller.response.BoardDetailResponse;
import com.newsvision.board.controller.response.BoardResponse;
import com.newsvision.board.entity.Board;
import com.newsvision.board.entity.BoardLike;
import com.newsvision.board.repository.BoardLikeRepository;
import com.newsvision.board.repository.BoardRepository;
import com.newsvision.category.entity.Categories;
import com.newsvision.category.repository.CategoryRepository;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public List<BoardResponse> getBoardsList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        List<Board> boards = boardRepository.findAll(pageable).getContent();
        return boards.stream().map(board -> {
            long likeCount = (board.getBoardLikes() != null) ? board.getBoardLikes().size() : 0; // 좋아요 수 계산
            long commentCount = (board.getComments() != null) ? board.getComments().size() : 0; // 댓글 수 계산
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

    @Transactional(readOnly = true)
    public BoardDetailResponse getBoardDetail(Long boardId) { // 게시글 상세 조회
        Board board = boardRepository.findById(boardId) // findById 사용으로 변경
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        // 좋아요 수 계산 - null 체크 추가
        long likeCount = (board.getBoardLikes() != null) ? board.getBoardLikes().size() : 0;
        // 댓글 수 계산 - null 체크 추가
        long commentCount = (board.getComments() != null) ? board.getComments().size() : 0;
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
    public BoardDetailResponse createBoard(Long userId, BoardCreateRequest request) { // 게시글 작성
        try {
        log.info("사용자 ID {} 로 사용자 찾기 시도", userId);
        User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        log.info("사용자 찾음: 사용자 이름 - {}", user.getUsername()); // 로그 추가
        Categories category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

            Board board = new Board();
            board.setTitle(request.getTitle());
            board.setContent(request.getContent());
            board.setCategory(category);
            board.setUser(user);
            board.setImage(request.getImage());
        log.info("게시글 저장 시도: 제목 - {}", board.getTitle()); // 로그 추가
            Board savedBoard = boardRepository.save(board);
        log.info("게시글 저장 성공! ID - {}", savedBoard.getId()); // 로그 추가
            return getBoardDetail(savedBoard.getId());
        }catch (Exception e) {
            System.out.println("ERROR 발생!!!: " + e.getMessage());  // System.out.println 추가
            e.printStackTrace();
            log.error("게시글 생성 중 예외 발생!",e);
            throw e;
        }

    }
    @Transactional
    public BoardDetailResponse updateBoard(Long boardId, Long userId, BoardUpdateRequest request) { // 게시글 수정
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        log.info("카테고리 ID {} 로 카테고리 찾기 시도", request.getCategoryId());
        Categories category = categoryRepository.findById(request.getCategoryId()) // 카테고리 ID로 카테고리 조회
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        log.info("카테고리 찾음: 카테고리 이름 - {}", category.getName()); // 로그 추가
        // 게시글 작성자와 수정 요청자가 동일인인지 확인 (본인 게시글만 수정 가능)
        if (!Objects.equals(board.getUser().getId(), userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED); // 권한 없음 에러
        }

        board.setTitle(request.getTitle());
        board.setContent(request.getContent());
        board.setCategory(category);
        board.setImage(request.getImage());

        Board updatedBoard = boardRepository.save(board);
        return getBoardDetail(updatedBoard.getId());
    }
    @Transactional
    public void deleteBoard(Long boardId, Long userId) { // 게시글 삭제
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        // 게시글 작성자 본인 또는 관리자만 삭제 가능하도록 권한 체크 (isAdminUser()는 관리자 권한 체크 함수)
        if (!Objects.equals(board.getUser().getId(), userId) /* && !isAdminUser(userId) */) {
            throw new CustomException(ErrorCode.UNAUTHORIZED); // 권한 없음 에러
        }

        boardRepository.delete(board); // 게시글 삭제
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




