package com.newsvision.mypage.service;

import com.newsvision.board.entity.Board;
import com.newsvision.board.service.BoardService;
import com.newsvision.board.service.CommentService;
import com.newsvision.mypage.dto.response.*;
import com.newsvision.news.entity.News;
import com.newsvision.news.entity.Scrap;
import com.newsvision.news.service.NewsService;
import com.newsvision.news.service.ScrapService;
import com.newsvision.notice.entity.Notice;
import com.newsvision.notice.service.NoticeService;
import com.newsvision.user.entity.Badge;
import com.newsvision.user.entity.Follow;
import com.newsvision.user.entity.User;
import com.newsvision.user.service.BadgeService;
import com.newsvision.user.service.FollowService;
import com.newsvision.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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
    private final ScrapService scrapService;
    private final NoticeService noticeService;

    public MypageInfoResponse getPortionUser(Long userId) {
        User user = userService.findByUserId(userId);
        int followerCount = followService.getCountFollower(user);
        int followingCount = followService.getCountFollowing(user);
        Badge badge = badgeService.getBadgeByUser(user);
        return new MypageInfoResponse(user, followerCount, followingCount, badge);
    }

    public OtherUserInfoResponse getPortionOtherUser(Long userId, Long logId) {
        User user = userService.findByUserId(userId);
        int followerCount = followService.getCountFollower(user);
        int followingCount = followService.getCountFollowing(user);
        Badge badge = badgeService.getBadgeByUser(user);
        boolean followed = userId != null && followService.existsFollow(logId, userId);
        return new OtherUserInfoResponse(user, followerCount, followingCount, badge, followed);
    }

    public Page<FollowResponse> getFollowers(Long id, Pageable pageable) {
        Page<Follow> followerPage = followService.findByFollowingId(id, pageable);
        return followerPage.map(follow -> FollowResponse.from(follow.getFollower()));
    }

    public Page<FollowResponse> getFollowing(Long id, Pageable pageable) {
        Page<Follow> followingPage = followService.findByFollowerId(id, pageable);
        return followingPage.map(follow -> FollowResponse.from(follow.getFollowing()));
    }

    public Page<UserNewsListResponse> getMypageNewsList(Long id, Pageable pageable) {
        Page<News> newsPage = newsService.getMypageNewsList(id, pageable);
        return newsPage.map(news -> {
            int likeCount = newsService.countByNews(news);
            return UserNewsListResponse.from(news, likeCount);
        });
    }

    public Page<UserBoardListResponse> getMypageBoardList(Long id, Pageable pageable) {
        Page<Board> boardPage = boardService.getMypageBoardList(id, pageable);
        return boardPage.map(board -> {
                    int likeCount = boardService.countByBoardId(board.getId());
                    int commentCount = commentService.countByBoardId(board.getId());
                    return UserBoardListResponse.from(board, likeCount, commentCount);
                });
    }

    public Page<UserScrapListResponse> getMypageScrapList(Long id, Pageable pageable) {
        Page<Scrap> scrapList = scrapService.getMypageScrapList(id, pageable);
        return scrapList.map(scrap -> UserScrapListResponse.from(scrap.getNews()));
    }

    public Page<UserNoticeListResponse> getMyPageNoticeList(Long id, Pageable pageable) {
        Page<Notice> noticeList = noticeService.getMypageNoticeService(id, pageable);
        return noticeList.map(UserNoticeListResponse::from);
    }
}
