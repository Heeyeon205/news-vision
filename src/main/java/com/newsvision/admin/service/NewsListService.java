package com.newsvision.admin.service;

import com.newsvision.news.dto.response.NewsResponse;
import com.newsvision.news.entity.News;
import com.newsvision.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsListService {
    private final NewsRepository newsRepository;

    public List<NewsResponse> getAllNews() {
        List<News> newsList = newsRepository.findAll(); // 정렬 필요 시 Sort 추가

        return newsList.stream()
                .map(news -> NewsResponse.of(
                        news,
                        0,       // likeCount → 좋아요 연관 관계 없으므로 기본값
                        false,   // liked → 인증 없이 false
                        false    // scraped → 인증 없이 false
                ))
                .collect(Collectors.toList());
    }

    public List<NewsResponse> getMaxAllNews() {
        List<News> newsList = newsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        return newsList.stream()
                .map(news -> NewsResponse.of(
                        news,
                        0,       // likeCount → 좋아요 연관 관계 없으므로 기본값
                        false,   // liked → 인증 없이 false
                        false    // scraped → 인증 없이 false
                ))
                .collect(Collectors.toList());
    }



}