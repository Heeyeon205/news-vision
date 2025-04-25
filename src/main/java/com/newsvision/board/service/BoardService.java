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
import com.newsvision.global.aws.FileUploaderService;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import com.newsvision.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    private final UserService userService;
    private final FileUploaderService fileUploaderService;

    public Board findById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

    }


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
            int likeCount = (board.getBoardLikes() != null) ? board.getBoardLikes().size() : 0;
            int commentCount = (board.getComments() != null) ? board.getComments().size() : 0;
            return new BoardResponse(board, likeCount, commentCount);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BoardDetailResponse getBoardDetail(Board board) { // 게시글 상세 조회
        // 좋아요 수 계산 - null 체크 추가
        int likeCount = (board.getBoardLikes() != null) ? board.getBoardLikes().size() : 0;
        // 댓글 수 계산 - null 체크 추가
        int commentCount = (board.getComments() != null) ? board.getComments().size() : 0;
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
    public BoardDetailResponse createBoard(Long userId, MultipartFile image, String content, Long categoryId) { // 게시글 작성
        try {
        log.info("사용자 ID {} 로 사용자 찾기 시도", userId);
        User user = userService.findByUserId(userId);
        String imageUrl = null;
        if(image != null && !image.isEmpty()){
            try{
                byte[] resizedBoardImage = resizeBoardImage(image);
                imageUrl = fileUploaderService.uploadBoardImage(resizedBoardImage,user.getId());
                log.info("이미지 업로드 성공: URL - {}", imageUrl);

            }catch(IOException e){
                log.error("이미지 처리 실패:{}", e.getMessage());
            }
        }

        log.info("사용자 찾음: 사용자 이름 - {}", user.getUsername());
        Categories category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

            Board board = new Board();
            board.setContent(content);
            board.setCategory(category);
            board.setUser(user);
            board.setImage(imageUrl);
            Board savedBoard = boardRepository.save(board);
            boardSearchService.saveBoard(board, 0, 0);
        log.info("게시글 저장 성공! ID - {}", savedBoard.getId()); // 로그 추가
            return getBoardDetail(savedBoard);
        }catch (Exception e) {
            System.out.println("ERROR 발생!!!: " + e.getMessage());  // System.out.println 추가
            e.printStackTrace();
            log.error("게시글 생성 중 예외 발생!",e);
            throw e;
        }

    }
    @Transactional
    public BoardDetailResponse updateBoard(Board board, Long userId,MultipartFile image,String content,Long categoryId) { // 게시글 수정
        User user = userService.findByUserId(userId);
        String imageUrl = null;
        if(image != null && !image.isEmpty()){
            try{
                byte[] resizedBoardImage = resizeBoardImage(image);
                imageUrl = fileUploaderService.uploadBoardImage(resizedBoardImage,user.getId());
                log.info("이미지 업로드 성공: URL - {}", imageUrl);

            }catch(IOException e){
                log.error("이미지 처리 실패:{}", e.getMessage());
            }
        }
        log.info("카테고리 ID {} 로 카테고리 찾기 시도", categoryId);
        Categories category = categoryRepository.findById(categoryId) // 카테고리 ID로 카테고리 조회
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        log.info("카테고리 찾음: 카테고리 이름 - {}", category.getName()); // 로그 추가
        // 게시글 작성자와 수정 요청자가 동일인인지 확인 (본인 게시글만 수정 가능)
        if (!Objects.equals(board.getUser().getId(), userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED); // 권한 없음 에러
        }

        board.setContent(content);
        board.setCategory(category);
        board.setImage(imageUrl);

        Board updatedBoard = boardRepository.save(board);
        boardSearchService.saveBoard(board, board.getBoardLikes().size(), board.getComments().size());
        return getBoardDetail(updatedBoard);
    }
    @Transactional
    public void deleteBoard(Long boardId, Long userId) { // 게시글 삭제
        Board board = findById(boardId);
        User user = userService.findByUserId(userId);

        // 게시글 작성자 본인 또는 관리자만 삭제 가능하도록 권한 체크 (isAdminUser()는 관리자 권한 체크 함수)
        if (!Objects.equals(board.getUser().getId(), userId) /* && !isAdminUser(userId) */) {
            throw new CustomException(ErrorCode.UNAUTHORIZED); // 권한 없음 에러
        }

        boardRepository.delete(board); // 게시글 삭제
        boardSearchService.deleteBoard(boardId);
    }



    @Transactional
    public void likeBoard(Board board, Long userId) {
        log.info("BoardService.likeBoard - 시작: boardId={}, userId={}", board.getId(), userId);
        try {
            log.info("BoardService.likeBoard - id로 Board 가져오는 중: {}", board.getId());
            log.info("BoardService.likeBoard - Board 찾음: {}", board.getId());

            log.info("BoardService.likeBoard - id로 User 가져오는 중: {}", userId);
            User user = userService.findByUserId(userId);
            log.info("BoardService.likeBoard - User 찾음: {}", user.getId());

            boolean alreadyLiked = boardLikeRepository.existsByBoardIdAndUserId(board.getId(), userId);
            log.info("BoardService.likeBoard - 이미 좋아요 눌렀는지 확인: {}", alreadyLiked);

            if (alreadyLiked) {
                log.info("BoardService.likeBoard - 이미 좋아요 누름, 좋아요 취소");
                boardLikeRepository.deleteByBoardIdAndUserId(board.getId(), userId);
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
    public void incrementViewCount(Board board) { // 조회수 증가 기능
        board.updateView(board.getView() + 1);
        boardRepository.save(board);
    }

    private byte[] resizeBoardImage(MultipartFile file) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                .size(530, 300)
                .outputFormat("jpg")
                .outputQuality(0.9)
                .toOutputStream(outputStream);
        return outputStream.toByteArray();
    }


}




