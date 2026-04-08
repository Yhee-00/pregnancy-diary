package com.oyh.pregnancydiary.service;

import com.oyh.pregnancydiary.entity.AiMessage;
import com.oyh.pregnancydiary.entity.Pregnancy;
import com.oyh.pregnancydiary.repository.AiMessageRepository;
import com.oyh.pregnancydiary.repository.PregnancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AiMessageService {

    private final AiMessageRepository repository;
    private final AiService aiService;
    private final PregnancyRepository pregnancyRepository;

    public String getMessage(Long pregnancyId, String date) {

        Pregnancy pregnancy = pregnancyRepository.findById(pregnancyId)
                .orElseThrow(() -> new RuntimeException("임신 정보 없음"));

        int week = calculateWeek(pregnancy.getStartDate(), date);

        // 캐싱
        return repository.findByWeek(week)
                .map(AiMessage::getContent)
                .orElseGet(() -> {
                    String message = aiService.generateMessage(week);

                    AiMessage saved = AiMessage.builder()
                            .week(week)
                            .content(message)
                            .build();

                    repository.save(saved);
                    return message;
                });
    }
    private int calculateWeek(LocalDate startDate, String date) {

        LocalDate currentDate = LocalDate.parse(date);

        long days = ChronoUnit.DAYS.between(startDate, currentDate);

        return (int) (days / 7) + 1;
    }
}
