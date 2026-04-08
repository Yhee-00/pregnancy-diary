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
        String prompt = "임신 " + week + "주차에 맞는 태교 문구를 2~3줄로 따뜻하게 작성해줘.";

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
