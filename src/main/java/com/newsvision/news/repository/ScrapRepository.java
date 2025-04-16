package com.newsvision.news.repository;

import com.newsvision.news.entity.News;
import com.newsvision.news.entity.Scrap;
import com.newsvision.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    boolean existsByUserAndNews(User user, News news);

    void deleteByUserAndNews(User user, News news);

    List<Scrap> findAllByUser(User user);
}

