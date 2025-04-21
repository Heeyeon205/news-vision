package com.newsvision.news.repository;

import com.newsvision.news.entity.NaverNews;
import com.newsvision.news.entity.News;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NaverNewsRepository extends JpaRepository<NaverNews, Long> {
    Optional<Object> findByLink(String link);
}