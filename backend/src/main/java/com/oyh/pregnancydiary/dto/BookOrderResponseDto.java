package com.oyh.pregnancydiary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookOrderResponseDto {
    private Long id;
    private String sweetbookBookId;
    private String sweetbookOrderId;
    private String status;
    private LocalDateTime createdAt;
}
