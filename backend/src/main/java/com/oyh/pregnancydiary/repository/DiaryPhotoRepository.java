package com.oyh.pregnancydiary.repository;

import com.oyh.pregnancydiary.entity.DiaryPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiaryPhotoRepository extends JpaRepository<DiaryPhoto, Long> {
    List<DiaryPhoto> findByDiaryEntryId(Long diaryEntryId);
    void deleteByDiaryEntryId(Long diaryEntryId);
    Optional<DiaryPhoto> findByFilePath(String filePath);
    void deleteByFilePath(String filePath);
}
