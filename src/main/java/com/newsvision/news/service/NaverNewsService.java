package com.newsvision.news.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.news.controller.request.NaverNewsSaveRequest;
import com.newsvision.news.controller.request.NewsCreateRequest;
import com.newsvision.news.dto.response.NaverNewsInfoResponse;
import com.newsvision.news.dto.response.NaverNewsSearchResponse;
import com.newsvision.news.entity.NaverNews;
import com.newsvision.news.entity.News;
import com.newsvision.news.repository.NaverNewsRepository;
import com.newsvision.news.repository.NewsRepository;
import com.newsvision.user.repository.UserRepository;
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

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NaverNewsService {
    @Value("${naver.client-id}")
    private String clientId;
    @Value("${naver.client-secret}")
    private String clientSecret;

    private final NaverNewsRepository naverNewsRepository;
    private final UserRepository userRepository;
    private final NewsRepository newsRepository;

    // 스프링 기본 HTTP 클라이언트
    private final RestTemplate restTemplate = new RestTemplate();
    // response.getBody()로 받은 JSON 문자열로 변환
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 검색어, 개수로 뉴스를 받아온다.
    public List<NaverNewsInfoResponse> searchNews(String query, int display) {
        String encodedQuery = UriUtils.encode(query, StandardCharsets.UTF_8);
        String url = "https://openapi.naver.com/v1/search/news.json?query=" + encodedQuery
                + "&display=" + display + "&start=1&sort=sim"; // sim(정확도), date(최신순)

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, request, String.class
            );
            NaverNewsSearchResponse result = objectMapper.readValue(response.getBody(), NaverNewsSearchResponse.class);
            return result.getItems();
        } catch (org.springframework.web.client.RestClientResponseException e) {
            log.error("HTTP 오류 - 상태 코드: {},", e.getRawStatusCode());
            log.error("HTTP 오류 - 바디: {}", e.getResponseBodyAsString());
            throw new RuntimeException("네이버 뉴스 API 호출 실패", e);
        } catch (Exception e) {
            log.error("JSON 파싱 실패", e);
            throw new RuntimeException("네이버 뉴스 응답 파싱 실패", e);
        }
    }

    @Transactional
    public Long saveNaverNews(NaverNewsSaveRequest request) {
        naverNewsRepository.findByLink(request.getLink()).ifPresent(news -> {
            throw new CustomException(ErrorCode.DUPLICATE_NAVER_NEWS);
        });
        LocalDateTime publishedAt = LocalDateTime.parse(request.getPubDate(), DateTimeFormatter.RFC_1123_DATE_TIME);
        NaverNews news = NaverNews.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .link(request.getLink())
                .publishedAt(publishedAt)
                .build();
        return naverNewsRepository.save(news).getId();
    }
}
