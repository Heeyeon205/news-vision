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
import com.newsvision.elasticsearch.service.BoardSearchService;
import com.newsvision.global.Utils.TimeUtil;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    private final BoardSearchService boardSearchService;

    public List<BoardResponse> getBoardsList(int page, int size, Long categoryId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Board> boardPage;

        if(categoryId != null){
            log.info("특정 카테고리 게시글 조회 - categoryId: {}", categoryId);
            log.info("BoardRepository.findByCategoryId 호출 전 - categoryId: {}, pageable: {}", categoryId, pageable);
            boardPage = boardRepository.findByCategoryId(categoryId, pageable); // 카테고리 ID로 조회
            log.info("BoardRepository.findByCategoryId 호출 후 - 반환된 Page 객체: {}", boardPage);
        }else{
            log.info("전체 게시글 조회");
            boardPage = boardRepository.findAll(pageable);
        }


        List<Board> boards = boardPage.getContent();
        return boards.stream().map(board -> {
            long likeCount = (board.getBoardLikes() != null) ? board.getBoardLikes().size() : 0; // 좋아요 수 계산
            long commentCount = (board.getComments() != null) ? board.getComments().size() : 0; // 댓글 수 계산
            return new BoardResponse(
                    board.getContent(),
                    board.getCategory().getId(),
                    board.getCreateAt(),
                    TimeUtil.formatRelativeTime(board.getCreateAt()),
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
                board.getContent(),
                board.getCategory().getId(),
                board.getCreateAt(),
                TimeUtil.formatRelativeTime(board.getCreateAt()),
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
            board.setContent(request.getContent());
            board.setCategory(category);
            board.setUser(user);
            board.setImage(request.getImage());
            Board savedBoard = boardRepository.save(board);
            boardSearchService.saveBoard(board);
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

        board.setContent(request.getContent());
        board.setCategory(category);
        board.setImage(request.getImage());

        Board updatedBoard = boardRepository.save(board);
        boardSearchService.saveBoard(board);
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
        boardSearchService.deleteBoard(boardId);
    }



    @Transactional
    public void likeBoard(Long boardId, Long userId) {
        log.info("BoardService.likeBoard - 시작: boardId={}, userId={}", boardId, userId);
        try {
            log.info("BoardService.likeBoard - id로 Board 가져오는 중: {}", boardId);
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> {
                        log.warn("BoardService.likeBoard - id로 Board를 찾을 수 없음: {}", boardId);
                        return new CustomException(ErrorCode.NOT_FOUND);
                    });
            log.info("BoardService.likeBoard - Board 찾음: {}", board.getId());

            log.info("BoardService.likeBoard - id로 User 가져오는 중: {}", userId);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        log.warn("BoardService.likeBoard - id로 User를 찾을 수 없음: {}", userId);
                        return new CustomException(ErrorCode.NOT_FOUND);
                    });
            log.info("BoardService.likeBoard - User 찾음: {}", user.getId());

            boolean alreadyLiked = boardLikeRepository.existsByBoardIdAndUserId(boardId, userId);
            log.info("BoardService.likeBoard - 이미 좋아요 눌렀는지 확인: {}", alreadyLiked);

            if (alreadyLiked) {
                log.info("BoardService.likeBoard - 이미 좋아요 누름, 좋아요 취소");
                boardLikeRepository.deleteByBoardIdAndUserId(boardId, userId);
                log.info("BoardService.likeBoard - 좋아요 취소 완료");
            } else {
                log.info("BoardService.likeBoard - 아직 좋아요 안 누름, 좋아요 추가");
                BoardLike boardLike = new BoardLike();
                boardLike.setBoard(board);
                boardLike.setUser(user);
                boardLikeRepository.save(boardLike);
                log.info("BoardService.likeBoard - 좋아요 추가 완료");
            }
            log.info("BoardService.likeBoard - 성공적으로 완료");
        } catch (CustomException e) {
            log.error("BoardService.likeBoard - CustomException 발생: {}", e.getErrorCode());
            throw e; // 예외 다시 던지기
        } catch (Exception e) {
            log.error("BoardService.likeBoard - 예기치 않은 예외 발생", e);
            throw e; // 예외 다시 던지기
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




