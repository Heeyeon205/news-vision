package com.newsvision.news.service;

import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.news.controller.response.NewsResponse;
import com.newsvision.news.controller.response.NewsSummaryResponse;
import com.newsvision.news.entity.News;
import com.newsvision.news.entity.NewsLike;
import com.newsvision.news.entity.Scrap;
import com.newsvision.news.repository.NewsLikeRepository;
import com.newsvision.news.repository.NewsRepository;
import com.newsvision.news.repository.ScrapRepository;
import com.newsvision.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final NewsLikeRepository newsLikeRepository;
    private final ScrapRepository scrapRepository;

    //
    public List<NewsSummaryResponse> getTop10RecentNewsOnlyByAdmin() {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        List<News> topNews = newsRepository.findTopNewsByAdminOnly(threeDaysAgo, PageRequest.of(0, 10));
        return topNews.stream()
                .map(NewsSummaryResponse::from)
                .toList();
    }

    @Transactional
    public NewsResponse getNewsDetail(Long newsId, User loginUser) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        news.setView(news.getView() + 1); // 조회수 증가

        int likeCount = newsLikeRepository.countByNews(news);
        boolean liked = loginUser != null && newsLikeRepository.existsByUserAndNews(loginUser, news);
        boolean scraped = false;//loginUser != null && ScrapRepository.existsByUserAndNews(loginUser, news);
        return NewsResponse.of(news, likeCount, liked, scraped);
    }


    @Transactional
    public void addLike(Long newsId, User user) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        boolean exists = newsLikeRepository.existsByUserAndNews(user, news);
        if (!exists) {
            NewsLike like = NewsLike.builder()
                    .news(news)
                    .user(user)
                    .build();
            newsLikeRepository.save(like);
        }
    }

    @Transactional
    public void removeLike(Long newsId, User user) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        newsLikeRepository.deleteByUserAndNews(user, news);
    }

    @Transactional
    public void addScrap(Long newsId, User user) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        boolean exists = scrapRepository.existsByUserAndNews(user, news);
        if (!exists) {
            Scrap scrap = Scrap.builder()
                    .news(news)
                    .user(user)
                    .build();
            scrapRepository.save(scrap);
        }
    }

    @Transactional
    public void removeScrap(Long newsId, User user) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        scrapRepository.deleteByUserAndNews(user, news);
    }


    public List<NewsSummaryResponse> getMyScrapList(User user) {
        return scrapRepository.findAllByUser(user).stream()
                .map(scrap -> NewsSummaryResponse.from(scrap.getNews()))
                .toList();
    }

    public List<NewsSummaryResponse> getCreatorNewsList() {
        return newsRepository.findAllByCreatorOrderByCreatedAtDesc()
                .stream()
                .map(NewsSummaryResponse::from)
                .toList();
    }

    public Page<NewsSummaryResponse> getNewsListByCreatedAt(Pageable pageable) {
        return newsRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(NewsSummaryResponse::from);
    }

    public Page<NewsSummaryResponse> getFilteredArticles(String type, Long categoryId, User user, Pageable pageable) {
        Page<News> result;

        switch (type) {
            case "popular":
                result = newsRepository.findAllOrderByLikeCountDesc(pageable);
                break;
            case "follow":
                if (user == null) throw new CustomException(ErrorCode.UNAUTHORIZED);
                result = newsRepository.findByFollowingUsers(user.getId(), pageable);
                break;
            case "category":
                result = newsRepository.findByCategoryId(categoryId, pageable);
                break;
            case "recent":
            default:
                result = newsRepository.findAllByOrderByCreatedAtDesc(pageable);
                break;
        }


        return result.map(NewsSummaryResponse::from);
    }

//    @Transactional
//    public void createNews(NewsRequestDto dto, Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("사용자 정보 없음"));
//
//        Category category = categoryRepository.findById(dto.getCategoryId())
//                .orElseThrow(() -> new IllegalArgumentException("카테고리 없음"));
//
//        News news = News.builder()
//                .title(dto.getTitle())
//                .content(dto.getContent())
//                .image(dto.getImage())
//                .category(category)
//                .user(user)
//                .createdAt(LocalDateTime.now())
//                .view(0)
//                .build();
//
//        newsRepository.save(news);
//    }


}
