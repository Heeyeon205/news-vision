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
import org.springframework.data.domain.PageRequest;
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

    public List<NewsSummaryResponse> getTop10RecentNews() {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        List<News> topNews = newsRepository.findTopNewsAfterDate(threeDaysAgo, PageRequest.of(0, 10));

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
    public boolean toggleLike(Long newsId, User loginUser) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        boolean alreadyLiked = newsLikeRepository.existsByUserAndNews(loginUser, news);

        if (alreadyLiked) {
            newsLikeRepository.deleteByUserAndNews(loginUser, news);
            return false; // 좋아요 취소
        } else {
            NewsLike like = NewsLike.builder()
                    .news(news)
                    .user(loginUser)
                    .build();
            newsLikeRepository.save(like);
            return true; // 좋아요 추가
        }
    }

    @Transactional
    public boolean toggleScrap(Long newsId, User user) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        boolean alreadyScrapped = scrapRepository.existsByUserAndNews(user, news);

        if (alreadyScrapped) {
            scrapRepository.deleteByUserAndNews(user, news);
            return false; // 취소
        } else {
            scrapRepository.save(Scrap.builder().user(user).news(news).build());
            return true; // 추가
        }
    }

    public List<NewsSummaryResponse> getMyScrapList(User user) {
        return scrapRepository.findAllByUser(user).stream()
                .map(scrap -> NewsSummaryResponse.from(scrap.getNews()))
                .toList();
    }

}
