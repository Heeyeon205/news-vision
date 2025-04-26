package com.newsvision.mypage;

import com.newsvision.board.entity.Board;
import com.newsvision.board.service.BoardService;
import com.newsvision.board.service.CommentService;
import com.newsvision.mypage.response.*;
import com.newsvision.news.entity.News;
import com.newsvision.news.entity.Scrap;
import com.newsvision.news.service.NewsService;
import com.newsvision.notice.Notice;
import com.newsvision.user.entity.Badge;
import com.newsvision.user.entity.User;
import com.newsvision.user.service.BadgeService;
import com.newsvision.user.service.FollowService;
import com.newsvision.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MypageService {
    private final UserService userService;
    private final FollowService followService;
    private final BadgeService badgeService;
    private final CommentService commentService;
    private final BoardService boardService;
    private final NewsService newsService;

    public MypageInfoResponse getPortionUser(Long userId) {
        User user = userService.findByUserId(userId);
        int followerCount = followService.getCountFollower(user);
        int followingCount = followService.getCountFollowing(user);
        Badge badge = badgeService.getBadgeByUser(user);
        return new MypageInfoResponse(user, followerCount, followingCount, badge);
    }

    public List<FollowResponse> getFollowers(Long id){
        User user = userService.findByUserId(id);
        return user.getFollowerList().stream()
                .map(follow -> FollowResponse.from(follow.getFollower()))
                .toList();
    }

    public List<FollowResponse> getFollowing(Long id){
        User user = userService.findByUserId(id);
        return user.getFollowingList().stream()
                .map(follow -> FollowResponse.from(follow.getFollowing()))
                .toList();
    }

    public List<UserBoardListResponse> getMypageBoardList(Long userId) {
        User user = userService.findByUserId(userId);
        List<Board> boardList = user.getBoardList();
        return boardList.stream()
                .map(board -> {
                    int likeCount = boardService.countByBoardId(board.getId());
                    int commentCount = commentService.countByBoardId(board.getId());
                    return UserBoardListResponse.from(board, likeCount, commentCount);
                }).toList();
    }

    public List<UserNewsListResponse> getMypageNewsList(Long id) {
        User user = userService.findByUserId(id);
        List<News> newsList = user.getNewsList();
        return newsList.stream()
                .map(news -> {
                    int likeCount = newsService.countByNews(news);
                    return UserNewsListResponse.from(news, likeCount);
                }).toList();
    }

    public List<UserScrapListResponse> getMypageScrapList(Long id) {
        User user = userService.findByUserId(id);
        List<Scrap> scrapList = user.getScrapList();
        return scrapList.stream()
                .map(scrap -> UserScrapListResponse.from(scrap.getNews()))
                .toList();
    }

    public List<UserNoticeListResponse> getMyPageNoticeList(Long id) {
        User user = userService.findByUserId(id);
        List<Notice> noticeList = user.getNoticeList();
        return noticeList.stream()
                .map(UserNoticeListResponse::from).toList();
    }
}
