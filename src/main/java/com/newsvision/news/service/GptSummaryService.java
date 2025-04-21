package com.newsvision.news.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptSummaryService {
    @Value("${openai.api-key}")
    private String openAiApiKey;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public String summarize(String originalText) {
        OkHttpClient client = new OkHttpClient();

        // 프롬프트
        String requestJson = """
            {
              "model": "gpt-3.5-turbo",
              "messages": [
                { "role": "system", "content": "뉴스 본문을 3줄로 요약해줘. 핵심 내용만 포함시켜줘." },
                { "role": "user", "content": "%s" }
              ],
              "temperature": 0.7
            }
            """.formatted(originalText);

        // HTTP 요청 생성
        RequestBody body = RequestBody.create(
                requestJson,
                MediaType.parse("application/json")
        );

        // 헤더 셋팅
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + openAiApiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        // 응답 처리
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("API 호출 실패 {}", response.code());
                throw new IOException("API 호출 실패: " + response.code());
            }

            // 응답 파싱
            String responseBody = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);
            return root.get("choices").get(0).get("message").get("content").asText().trim();

        } catch (Exception e) {
            log.error("API 요약 실패");
            e.printStackTrace();
            return "요약 실패";
        }
    }
}
