package com.newsvision.news.repository;

import com.newsvision.news.entity.GptNews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GptNewsRepository extends JpaRepository<GptNews, Long> {
    Optional<GptNews> findByNewsId(Long newsId);
}
