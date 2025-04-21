package com.newsvision.news.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.newsvision.news.entity.GptNews;
import com.newsvision.news.repository.GptNewsRepository;
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
    private final GptNewsRepository gptNewsRepository;

    public String createSummary(Long newsId, String title, String content) {
        return gptNewsRepository.findByNewsId(newsId)
                .map(GptNews::getSummary)
                .orElseGet(() -> {
                    try {
                        String summary = useChatGptSummary(content);
                        GptNews gptNews = GptNews.builder()
                                .newsId(newsId)
                                .title(title)
                                .summary(summary)
                                .build();
                        gptNewsRepository.save(gptNews);
                        return summary;
                    } catch (Exception e) {
                        log.error("요약 실패", e);
                        return "요약 실패";
                    }
                });
    }

    private String useChatGptSummary(String content) throws IOException {
        OkHttpClient client = new OkHttpClient();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode requestJson = objectMapper.createObjectNode();
        requestJson.put("model", "gpt-3.5-turbo");
        requestJson.put("temperature", 0.7);

        ArrayNode messages = objectMapper.createArrayNode();
        messages.add(objectMapper.createObjectNode()
                .put("role", "system")
                .put("content", "아래 뉴스 본문을 150자 이내로 요약해줘. 핵심 정보만 뽑아주고, 불필요한 배경 설명은 생략해."));
        messages.add(objectMapper.createObjectNode()
                .put("role", "user")
                .put("content", content));

        requestJson.set("messages", messages);

        RequestBody body = RequestBody.create(
                objectMapper.writeValueAsString(requestJson),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + openAiApiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("API 호출 실패: " + response.code());
            }
            String responseBody = response.body().string();
            JsonNode root = objectMapper.readTree(responseBody);
            return root.get("choices").get(0).get("message").get("content").asText().trim();
        }
    }
}
