package com.oyh.pregnancydiary.repository;

import com.oyh.pregnancydiary.entity.Pregnancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PregnancyRepository extends JpaRepository<Pregnancy, Long> {
    List<Pregnancy> findByUserId(Long userId);
    Optional<Pregnancy> findFirstByUserIdOrderByCreatedAtDesc(Long userId);
}
