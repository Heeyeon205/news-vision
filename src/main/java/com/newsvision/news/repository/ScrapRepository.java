package com.newsvision.news.repository;

import com.newsvision.news.entity.News;
import com.newsvision.news.entity.Scrap;
import com.newsvision.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    //news와 user로 스크랩했었는지 체크
    boolean existsByUserAndNews(User user, News news);

    //news와 user로 해당 스크랩 삭제
    void deleteByUserAndNews(User user, News news);

    //특정 유저가 스크랩 한 뉴스 목록 찾기
    List<Scrap> findAllByUser(User user);
}

