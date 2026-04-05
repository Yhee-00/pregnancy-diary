package com.oyh.pregnancydiary.repository;

import com.oyh.pregnancydiary.entity.DiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Long> {
    List<DiaryEntry> findByPregnancyIdOrderByEntryDateAsc(Long pregnancyId);
    Optional<DiaryEntry> findByPregnancyIdAndEntryDate(Long pregnancyId, LocalDate entryDate);
    long countByPregnancyId(Long pregnancyId);

}
