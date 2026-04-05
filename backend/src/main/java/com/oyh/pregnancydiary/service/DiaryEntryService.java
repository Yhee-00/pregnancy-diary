package com.oyh.pregnancydiary.service;

import com.oyh.pregnancydiary.dto.DiaryEntryRequestDto;
import com.oyh.pregnancydiary.dto.DiaryEntryResponseDto;
import com.oyh.pregnancydiary.entity.DiaryEntry;
import com.oyh.pregnancydiary.entity.Pregnancy;
import com.oyh.pregnancydiary.repository.DiaryEntryRepository;
import com.oyh.pregnancydiary.repository.PregnancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryEntryService {

    private final DiaryEntryRepository diaryEntryRepository;
    private final PregnancyRepository pregnancyRepository;

    public DiaryEntryResponseDto create(Long pregnancyId, DiaryEntryRequestDto dto) {
        Pregnancy pregnancy = pregnancyRepository.findById(pregnancyId)
                .orElseThrow(() -> new RuntimeException("임신 정보를 찾을 수 없습니다."));

        DiaryEntry entry = DiaryEntry.builder()
                .pregnancy(pregnancy)
                .entryDate(dto.getEntryDate())
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();

        return toResponseDto(diaryEntryRepository.save(entry));
    }

    public List<DiaryEntryResponseDto> findByPregnancyId(Long pregnancyId) {
        return diaryEntryRepository.findByPregnancyIdOrderByEntryDateAsc(pregnancyId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public DiaryEntryResponseDto findById(Long entryId) {
        DiaryEntry entry = diaryEntryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("일기를 찾을 수 없습니다."));
        return toResponseDto(entry);
    }

    @Transactional
    public DiaryEntryResponseDto update(Long entryId, DiaryEntryRequestDto dto) {
        DiaryEntry entry = diaryEntryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("일기를 찾을 수 없습니다."));

        entry.setTitle(dto.getTitle());
        entry.setContent(dto.getContent());

        return toResponseDto(entry);
    }

    @Transactional
    public void delete(Long entryId) {
        diaryEntryRepository.deleteById(entryId);
    }

    private DiaryEntryResponseDto toResponseDto(DiaryEntry entry) {
        List<String> photoUrls = entry.getPhotos().stream()
                .map(photo -> "/uploads/" + photo.getFilePath())
                .toList();

        return DiaryEntryResponseDto.builder()
                .id(entry.getId())
                .entryDate(entry.getEntryDate())
                .title(entry.getTitle())
                .content(entry.getContent())
                .photoUrls(photoUrls)
                .createdAt(entry.getCreatedAt())
                .build();
    }
}
