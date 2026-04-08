package com.oyh.pregnancydiary.controller;

import com.oyh.pregnancydiary.entity.AiMessage;
import com.oyh.pregnancydiary.service.AiMessageService;
import com.oyh.pregnancydiary.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiController {
    private final AiMessageService aiMessageService;

    @GetMapping("/message")
    public String getMessage(@RequestParam Long pregnancyId, @RequestParam String date) {
        return aiMessageService.getMessage(pregnancyId, date);
    }
}
