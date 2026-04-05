package com.oyh.pregnancydiary.controller;

import com.oyh.pregnancydiary.dto.BookOrderRequestDto;
import com.oyh.pregnancydiary.dto.BookOrderResponseDto;
import com.oyh.pregnancydiary.service.BookPrintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookPrintService bookPrintService;

    @PostMapping("/order")
    public ResponseEntity<BookOrderResponseDto> createOrder(@RequestBody BookOrderRequestDto dto){
        return ResponseEntity.ok(bookPrintService.createOrder(dto));
    }

    @GetMapping("/order/{pregnancyId}")
    public ResponseEntity<List<BookOrderResponseDto>> findOrders(@PathVariable Long pregnancyId){
        return ResponseEntity.ok(bookPrintService.findByPregnancyId(pregnancyId));
    }
}
