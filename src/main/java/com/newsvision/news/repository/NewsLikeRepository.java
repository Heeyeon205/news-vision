package com.newsvision.news.repository;


import com.newsvision.news.entity.News;
import com.newsvision.news.entity.NewsLike;
import com.newsvision.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsLikeRepository extends JpaRepository<NewsLike, Long> {
    boolean existsByUserAndNews(User user, News news);

    void deleteByUserAndNews(User user, News news);

    int countByNews(News news);
}
