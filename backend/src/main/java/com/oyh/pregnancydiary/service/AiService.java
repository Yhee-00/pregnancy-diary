package com.oyh.pregnancydiary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {
    @Value("${openai.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.openai.com/v1")
            .build();

    public String generateMessage(int week){
        String prompt =
        "임신 " + week + "주차 태교 문구를 작성해줘. " +
        "부모가 아이에게 말하는 따뜻한 말투로, '~해', '~하자' 형태의 자연스러운 한국어 한 문장으로만 작성해줘. " +
        "설명 없이 문장만 출력해줘.";

        Map<String, Object> body = Map.of(
                "model", "gpt-4o-mini",
                "input", prompt
        );

        Map response = webClient.post()
                .uri("/responses")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        System.out.println(response);
        List output = (List) response.get("output");
        Map first = (Map) output.get(0);
        List content = (List) first.get("content");
        Map text = (Map) content.get(0);

        return (String) text.get("text");
    }
}
