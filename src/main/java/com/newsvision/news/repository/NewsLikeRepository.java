package com.newsvision.news.repository;


import com.newsvision.news.entity.News;
import com.newsvision.news.entity.NewsLike;
import com.newsvision.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsLikeRepository extends JpaRepository<NewsLike, Long> {
    //user와 news로 좋아요 체크
    boolean existsByUserAndNews(User user, News news);
    //user와 news로 해당 좋아요 삭제
    void deleteByUserAndNews(User user, News news);
    //news 좋아요 개수 체크
    int countByNews(News news);

    void deleteAllByNews(News news);

    boolean existsByNewsIdAndUserId(Long newsId, Long userId);
}
