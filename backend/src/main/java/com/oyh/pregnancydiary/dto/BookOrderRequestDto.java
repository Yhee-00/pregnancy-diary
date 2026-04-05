package com.oyh.pregnancydiary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookOrderRequestDto {
    private Long pregnancyId;
    private String recipientName;
    private String recipientPhone;
    private String address;
    private String addressDetail;
    private String zipCode;
}
