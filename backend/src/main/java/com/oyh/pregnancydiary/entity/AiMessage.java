package com.oyh.pregnancydiary.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ai_message")
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class AiMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int week;

    @Column(length = 1000)
    private String content;
}
