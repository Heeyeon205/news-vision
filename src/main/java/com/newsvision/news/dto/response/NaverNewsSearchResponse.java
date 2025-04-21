package com.newsvision.news.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaverNewsSearchResponse {
    private String lastBuildDate;   // 응답 생성 시각
    private int total;              // 전체 검색 결과 수
    private int start;              // 시작 index
    private int display;            // 반환된 결과 수
    private List<NaverNewsInfoResponse> items;  // 뉴스 리스트
}
