package com.newsvision.news.repository;

import com.newsvision.news.controller.response.NewsResponse;
import com.newsvision.news.entity.News;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("""
    SELECT n FROM News n
    WHERE n.createdAt >= :fromDate
    ORDER BY (
        SELECT COUNT(nl) FROM NewsLike nl WHERE nl.news = n
    ) DESC
""")
    List<News> findTopNewsAfterDate(LocalDateTime fromDate, Pageable pageable);


    Optional<News> findById(Long id);

}
