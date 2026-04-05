package com.oyh.pregnancydiary.controller;

import com.oyh.pregnancydiary.dto.PregnancyRequestDto;
import com.oyh.pregnancydiary.dto.PregnancyResponseDto;
import com.oyh.pregnancydiary.service.PregnancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pregnancy")
@RequiredArgsConstructor
public class PregnancyController {

    private final PregnancyService pregnancyService;

    @PostMapping
    public ResponseEntity<PregnancyResponseDto> create(@RequestBody PregnancyRequestDto dto){
        // 임시로 userId 1L 고정 (나중에 인증 추가 시 변경)
        return ResponseEntity.ok(pregnancyService.create(1L, dto));
    }

    @GetMapping
    public ResponseEntity<List<PregnancyResponseDto>> findAll(){
        return ResponseEntity.ok(pregnancyService.findByUserId(1L));
    }

    @GetMapping("/{pregnancyId}")
    public ResponseEntity<PregnancyResponseDto> findById(@PathVariable Long pregnancyId){
        return ResponseEntity.ok(pregnancyService.findById(pregnancyId));
    }
}
