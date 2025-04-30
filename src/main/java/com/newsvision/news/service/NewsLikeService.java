package com.newsvision.news.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.news.dto.request.NaverNewsSaveRequest;
import com.newsvision.news.dto.response.NaverNewsInfoResponse;
import com.newsvision.news.dto.response.NaverNewsSearchResponse;
import com.newsvision.news.entity.NaverNews;
import com.newsvision.news.entity.News;
import com.newsvision.news.entity.NewsLike;
import com.newsvision.news.repository.NaverNewsRepository;
import com.newsvision.news.repository.NewsLikeRepository;
import com.newsvision.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsLikeService {
    private final NewsLikeRepository newsLikeRepository;

    public boolean existsLike(News news, User user) {
        return newsLikeRepository.existsByUserAndNews(user, news);
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
}
