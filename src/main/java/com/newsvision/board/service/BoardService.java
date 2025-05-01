package com.newsvision.board.service;

import com.newsvision.board.dto.response.*;
import com.newsvision.board.entity.Board;
import com.newsvision.board.repository.BoardLikeRepository;
import com.newsvision.board.repository.BoardRepository;
import com.newsvision.category.Categories;
import com.newsvision.category.CategoryRepository;
import com.newsvision.category.CategoryResponse;
import com.newsvision.category.CategoryService;
import com.newsvision.elasticsearch.service.BoardSearchService;
import com.newsvision.global.aws.FileUploaderService;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.user.entity.User;
import com.newsvision.user.service.FollowService;
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
    private final CategoryRepository categoryRepository;
    private final BoardSearchService boardSearchService;
    private final UserService userService;
    private final FileUploaderService fileUploaderService;
    private final CommentService commentService;
    private final CategoryService categoryService;
    private final BoardLikeService boardLikeService;
    private final FollowService followService;

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
    public BoardDetailResponse getBoardDetail(Board board, Long userId) {
        int likeCount = (board.getBoardLikes() != null) ? board.getBoardLikes().size() : 0;
        int commentCount = (board.getComments() != null) ? board.getComments().size() : 0;
        boolean isLike = userId != null && boardLikeService.existsByBoardIdAndUserId(board.getId(), userId);
        boolean followed = userId != null && followService.existsFollow(userId, board.getUser().getId());
        List<CommentResponse> comments  = commentService.getCommentsByBoardId(board.getId());
        return new BoardDetailResponse(board, likeCount, commentCount, comments, isLike, followed);
    }

    @Transactional(readOnly = true)
    public BoardCreateResponse getBoardCreate(Board board) {
        int likeCount = (board.getBoardLikes() != null) ? board.getBoardLikes().size() : 0;
        // 댓글 수 계산 - null 체크 추가
        int commentCount = (board.getComments() != null) ? board.getComments().size() : 0;

        List<CommentResponse> comments = commentService.getCommentsByBoardId(board.getId());
        return new BoardCreateResponse(board, likeCount, commentCount, comments);
    }

    @Transactional
    public BoardCreateResponse createBoard(Long userId, MultipartFile image, String content, Long categoryId) { // 게시글 작성
        if(content.isEmpty()){
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
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
            return getBoardCreate(savedBoard);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("게시글 생성 중 예외 발생!",e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public BoardUpdateResponse getBoardUpdate(Board board) { // 게시글 상세 조회
        // 좋아요 수 계산 - null 체크 추가
        int likeCount = (board.getBoardLikes() != null) ? board.getBoardLikes().size() : 0;
        // 댓글 수 계산 - null 체크 추가
        int commentCount = (board.getComments() != null) ? board.getComments().size() : 0;

        List<CategoryResponse> categoryies = categoryService.findAll();
        List<CommentResponse> comments = commentService.getCommentsByBoardId(board.getId());
        return new BoardUpdateResponse(board, likeCount, commentCount, comments, categoryies);
    }

    @Transactional
    public BoardUpdateResponse updateBoard(Board board, Long userId, MultipartFile image, String content, Long categoryId) { // 게시글 수정
        User user = userService.findByUserId(userId);

        String imageUrl = null;
        if(image != null && !image.isEmpty()){
            String oldImageUrl = board.getImage();
            try{
                byte[] resizedBoardImage = resizeBoardImage(image);
                imageUrl = fileUploaderService.uploadBoardImage(resizedBoardImage,user.getId());
                board.setImage(imageUrl);

                if(oldImageUrl != null && !oldImageUrl.isEmpty()){
                    fileUploaderService.deleteFile(oldImageUrl);
                }
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

        Board updatedBoard = boardRepository.save(board);
        boardSearchService.saveBoard(board, board.getBoardLikes().size(), board.getComments().size());
        return getBoardUpdate(updatedBoard);
    }
    @Transactional
    public void deleteBoard(Long boardId, Long userId) { // 게시글 삭제
        Board board = findById(boardId);
        User user = userService.findByUserId(userId);

        // 게시글 작성자 본인 또는 관리자만 삭제 가능하도록 권한 체크 (isAdminUser()는 관리자 권한 체크 함수)
        if (!Objects.equals(board.getUser().getId(), userId) /* && !isAdminUser(userId) */) {
            throw new CustomException(ErrorCode.UNAUTHORIZED); // 권한 없음 에러
        }

        String imageUrl = board.getImage();
        if(imageUrl != null && !imageUrl.isEmpty()){
            try{
                fileUploaderService.deleteFile(imageUrl);
                log.info("S3에서 이미지 삭제 성공: URL - {}", imageUrl);
            }catch (Exception e){
                log.error("S3 이미지 삭제 실패: URL - {}, 오류: {}", imageUrl, e.getMessage());
            }
        }
        boardRepository.delete(board); // 게시글 삭제
        boardSearchService.deleteBoard(boardId);
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

    public int countByBoardId(Long id) {
        return boardLikeRepository.countByBoardId(id);
    }

    @Transactional
    public void addLike(Long boardId, Long userId) {
        if(boardLikeService.existsByBoardIdAndUserId(boardId, userId)) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        Board board = findById(boardId);
        User user = userService.findByUserId(userId);
        boardLikeService.save(board, user);
    }

    @Transactional
    public void removeLike(Long boardId, Long userId) {
        if(!boardLikeService.existsByBoardIdAndUserId(boardId, userId)) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        boardLikeService.delete(boardId, userId);
    }
}




