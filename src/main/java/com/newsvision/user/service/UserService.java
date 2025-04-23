package com.newsvision.user.service;

import com.newsvision.admin.service.CategoriesService;
import com.newsvision.board.entity.Board;
import com.newsvision.board.repository.BoardLikeRepository;
import com.newsvision.board.repository.CommentRepository;
import com.newsvision.category.repository.CategoryRepository;
import com.newsvision.global.aws.S3Uploader;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.mypage.dto.response.MypageInfoResponse;
import com.newsvision.news.entity.News;
import com.newsvision.news.entity.Scrap;
import com.newsvision.news.repository.NewsLikeRepository;
import com.newsvision.notice.Notice;
import com.newsvision.user.dto.request.JoinUserRequest;
import com.newsvision.user.dto.response.*;
import com.newsvision.user.entity.Badge;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    @Value("${custom.default-image-url}")
    private String defaultProfileImage;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;
    private final CommentRepository commentRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final NewsLikeRepository newsLikeRepository;

    public User findByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Transactional
    public void save(JoinUserRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        if(request.getUsername().equals("admin")){
            User user = User.builder()
                    .username(request.getUsername())
                    .password(encodedPassword)
                    .email(request.getEmail())
                    .image(defaultProfileImage)
                    .nickname(request.getNickname())
                    .role(User.Role.ROLE_ADMIN)
                    .build();
            userRepository.save(user);
        }
    }

    @Transactional
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    public UpdateUserResponse userInfo(Long id) {
        User user = findByUserId(id);
        log.warn(user.toString());
        return new UpdateUserResponse(user.getImage(), user.getNickname(), user.getIntroduce());
    }

    @Transactional
    public void updateUserProfile(Long id, MultipartFile image, String nickname, String introduce) {
        User user = findByUserId(id);
        if (image != null && !image.isEmpty()) {
            String keyName = "profile/" + user.getId() + "_" + UUID.randomUUID();
            String imageUrl = s3Uploader.upload(image, keyName);
            user.updateImage(imageUrl);
        }
        if (nickname != null && !nickname.equals(user.getNickname())) {
            user.updateNickname(nickname);
        }
        if (introduce != null && !introduce.equals(user.getIntroduce())) {
            user.updateIntroduce(introduce);
        }
    }

    public List<UserBoardListResponse> getMypageBoardList(Long userId) {
        User user = findByUserId(userId);
        List<Board> boardList = user.getBoardList();
        return boardList.stream()
                .map(board -> {
                    int likeCount = boardLikeRepository.countByBoardId(board.getId());
                    int commentCount = commentRepository.countByBoardId(board.getId());
                    return UserBoardListResponse.from(board, likeCount, commentCount);
                }).toList();
    }

    public List<UserNewsListResponse> getMypageNewsList(Long id) {
        User user = findByUserId(id);
        List<News> newsList = user.getNewsList();
        return newsList.stream()
                .map(news -> {
                    int likeCount = newsLikeRepository.countByNews(news);
                    return UserNewsListResponse.from(news, likeCount);
                }).toList();
    }

    public List<UserScrapListResponse> getMypageScrapList(Long id) {
        User user = findByUserId(id);
        List<Scrap> scrapList = user.getScrapList();
        return scrapList.stream()
                .map(scrap -> UserScrapListResponse.from(scrap.getNews()))
                .toList();
    }

    public void validateRole(String role) {
        if (!("ROLE_ADMIN".equals(role) || "ROLE_CREATOR".equals(role))) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
    }

    public List<UserNoticeListResponse> getMyPageNoticeList(Long id) {
        User user = findByUserId(id);
        List<Notice> noticeList = user.getNoticeList();
        return noticeList.stream()
                .map(UserNoticeListResponse::from).toList();
    }

    @Transactional
    public void updateIsPaidOrUser(User user){
        user.updateIsPaid(true);
        userRepository.save(user);
    }
}
