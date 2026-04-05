package com.oyh.pregnancydiary.controller;

import com.oyh.pregnancydiary.dto.DiaryEntryRequestDto;
import com.oyh.pregnancydiary.dto.DiaryEntryResponseDto;
import com.oyh.pregnancydiary.service.DiaryEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryEntryController {

    private final DiaryEntryService diaryEntryService;

    @PostMapping("/{pregnancyId}")
    public ResponseEntity<DiaryEntryResponseDto> create(@PathVariable Long pregnancyId, @RequestBody DiaryEntryRequestDto dto){
        return ResponseEntity.ok(diaryEntryService.create(pregnancyId, dto));
    }

    @GetMapping("/{pregnancyId}")
    public ResponseEntity<List<DiaryEntryResponseDto>> findAll(@PathVariable Long pregnancyId){
        return ResponseEntity.ok(diaryEntryService.findByPregnancyId(pregnancyId));
    }

    @GetMapping("/entry/{entryId}")
    public ResponseEntity<DiaryEntryResponseDto> findById(@PathVariable Long entryId){
        return ResponseEntity.ok(diaryEntryService.findById(entryId));
    }

    @PutMapping("/entry/{entryId}")
    public ResponseEntity<DiaryEntryResponseDto> update(@PathVariable Long entryId, @RequestBody DiaryEntryRequestDto dto){
        return ResponseEntity.ok(diaryEntryService.update(entryId, dto));
    }

    @DeleteMapping("/entry/{entryId}")
    public ResponseEntity<Void> delete(@PathVariable Long entryId){
        diaryEntryService.delete(entryId);
        return ResponseEntity.noContent().build();
    }
}
