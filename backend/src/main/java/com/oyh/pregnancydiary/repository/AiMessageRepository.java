package com.oyh.pregnancydiary.repository;

import com.oyh.pregnancydiary.entity.AiMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AiMessageRepository extends JpaRepository<AiMessage, Long> {
    Optional<AiMessage> findByWeek(int week);
}
