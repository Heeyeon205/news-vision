package com.newsvision.news.service;


import com.newsvision.news.entity.News;
import com.newsvision.news.entity.NewsLike;
import com.newsvision.news.repository.NewsLikeRepository;
import com.newsvision.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsLikeService {
    private final NewsLikeRepository newsLikeRepository;

    public boolean existsLike(Long newsId, Long userId) {
        return newsLikeRepository.existsByNewsIdAndUserId(newsId, userId);
    }

    public int findLikeCountByNews(News news) {
        return newsLikeRepository.countByNews(news);
    }

    @Transactional
    public void addLike(News news, User user) {
       NewsLike newsLike = NewsLike.builder()
               .news(news)
               .user(user)
               .build();
       newsLikeRepository.save(newsLike);
    }

    @Transactional
    public void removeLike(News news, User user) {
        newsLikeRepository.deleteByUserAndNews(user, news);
    }

    public int countLikeByNews(News news) {
        return newsLikeRepository.countByNews(news);
    }
}
