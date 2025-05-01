package com.newsvision.news.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsvision.global.Utils.TimeUtil;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.news.dto.request.NaverNewsSaveRequest;
import com.newsvision.news.dto.response.NaverNewsInfoResponse;
import com.newsvision.news.dto.response.NaverNewsSearchResponse;
import com.newsvision.news.entity.NaverNews;
import com.newsvision.news.repository.NaverNewsRepository;
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
public class  NaverNewsService {
    @Value("${naver.client-id}")
    private String clientId;
    @Value("${naver.client-secret}")
    private String clientSecret;

    private final NaverNewsRepository naverNewsRepository;
    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();

    public List<NaverNewsInfoResponse> searchNews(String query, int display) {
        display = 30;
        String encodedQuery = UriUtils.encode(query, StandardCharsets.UTF_8);
        String url = "https://openapi.naver.com/v1/search/news.json?query=" + encodedQuery
                + "&display=" + display + "&start=1&sort=date"; // sim(정확도), date(최신순)

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, request, String.class
            );
            NaverNewsSearchResponse result = objectMapper.readValue(response.getBody(), NaverNewsSearchResponse.class);

            return result.getItems().stream()
                    .map(item -> NaverNewsInfoResponse.builder()
                            .id(item.getId())
                            .title(decodeDescription(item.getTitle()))
                            .description(decodeDescription(item.getDescription()))
                            .link(decodeDescription(item.getLink()))
                            .originallink(decodeDescription(item.getOriginallink()))
                            .pubDate(TimeUtil.formatRelativeTime(TimeUtil.stringToLocalDateTime(item.getPubDate())))
                            .build())
//                    .filter(news ->
//                            news.getTitle().toLowerCase().contains(query.toLowerCase()) ||
//                                    news.getDescription().toLowerCase().contains(query.toLowerCase())
//                    )
                    .distinct()
                    .toList();
        } catch (org.springframework.web.client.RestClientResponseException e) {
            log.error("error: {},", e.getRawStatusCode());
            log.error("error {}", e.getResponseBodyAsString());
            throw new CustomException(ErrorCode.NOT_FOUND);
        } catch (Exception e) {
            log.error("error_{}", e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
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
                .link(request.getLink())
                .publishedAt(publishedAt)
                .build();
        return naverNewsRepository.save(news).getId();
    }

    private String decodeDescription(String text) {
        try {
            String decoded = URLDecoder.decode(text, StandardCharsets.UTF_8);
            return decoded.replaceAll("<[^>]*>", "");
        } catch (Exception e) {
            log.error("디코딩 실패", e);
            return text;
        }
    }

    @Transactional
    public void deleteNaverNews(Long id) {
        naverNewsRepository.deleteById(id);
    }
}
