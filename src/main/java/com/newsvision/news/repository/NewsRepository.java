package com.newsvision.news.repository;

import com.newsvision.news.controller.response.NewsResponse;
import com.newsvision.news.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {

    // 메인에서 공식뉴스 3일안 뉴스 좋아요 탑10 보여주기
    @Query("""
    SELECT n FROM News n
    WHERE n.createdAt >= :fromDate
    AND n.user.role = 'ADMIN'
    ORDER BY (SELECT COUNT(nl) FROM NewsLike nl WHERE nl.news = n) DESC
""")
    List<News> findTopNewsByAdminOnly(LocalDateTime fromDate, Pageable pageable);

    // 크리에이터가 작성한 사설 등록일순으로 보여주기
    @Query("""
    SELECT n FROM News n
    WHERE n.user.role = 'CREATOR'
    ORDER BY n.createdAt DESC
""")
    List<News> findAllByCreatorOrderByCreatedAtDesc();

    // 공식뉴스 등록일 순으로 보여주기 (무한스크롤)
    Page<News> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 뉴스id로 뉴스 상세보기
    Optional<News> findById(Long id);

}
