package com.oyh.pregnancydiary.service;

import com.oyh.pregnancydiary.dto.PregnancyRequestDto;
import com.oyh.pregnancydiary.dto.PregnancyResponseDto;
import com.oyh.pregnancydiary.entity.Pregnancy;
import com.oyh.pregnancydiary.entity.User;
import com.oyh.pregnancydiary.repository.DiaryEntryRepository;
import com.oyh.pregnancydiary.repository.PregnancyRepository;
import com.oyh.pregnancydiary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PregnancyService {
    private final PregnancyRepository pregnancyRepository;
    private final UserRepository userRepository;
    private final DiaryEntryRepository diaryEntryRepository;

    public PregnancyResponseDto create(Long userId, PregnancyRequestDto dto){
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("유저를 찾을 수 없습니다."));
        Pregnancy pregnancy = Pregnancy.builder()
                .user(user)
                .startDate(dto.getStartDate())
                .dueDate(dto.getDueDate())
                .babyNickname(dto.getBabyNickname())
                .build();

        return toResponseDto(pregnancyRepository.save(pregnancy));
    }

    public List<PregnancyResponseDto> findByUserId(Long userId){
        return pregnancyRepository.findByUserId(userId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public PregnancyResponseDto findById(Long pregnancyId){
        Pregnancy pregnancy = pregnancyRepository.findById(pregnancyId).orElseThrow(() -> new RuntimeException("임신 정보를 찾을 수 없습니다."));
        return toResponseDto(pregnancy);
    }
    private PregnancyResponseDto toResponseDto(Pregnancy pregnancy){
        long totalDays = ChronoUnit.DAYS.between(pregnancy.getStartDate(), pregnancy.getDueDate());
        long writtenDays = diaryEntryRepository.countByPregnancyId(pregnancy.getId());

        return PregnancyResponseDto.builder()
                .id(pregnancy.getId())
                .startDate(pregnancy.getStartDate())
                .dueDate(pregnancy.getDueDate())
                .babyNickname(pregnancy.getBabyNickname())
                .totalDays(totalDays)
                .writtenDays(writtenDays)
                .createdAt(pregnancy.getCreatedAt())
                .build();
    }
}
