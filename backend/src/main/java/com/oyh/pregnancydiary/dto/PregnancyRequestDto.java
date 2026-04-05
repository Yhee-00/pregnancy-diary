package com.oyh.pregnancydiary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PregnancyRequestDto {

    private LocalDate startDate;
    private LocalDate dueDate;
    private String babyNickname;
}
