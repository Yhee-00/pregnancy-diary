package com.oyh.pregnancydiary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PregnancyResponseDto {
    private Long id;
    private LocalDate startDate;
    private LocalDate dueDate;
    private String babyNickname;
    private long totalDays;
    private long writtenDays;
    private LocalDateTime createdAt;
}
