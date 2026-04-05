package com.oyh.pregnancydiary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiaryEntryResponseDto {
    private Long id;
    private LocalDate entryDate;
    private String title;
    private String content;
    private List<String> photoUrls;
    private LocalDateTime createdAt;
}
