package com.oyh.pregnancydiary.controller;

import com.oyh.pregnancydiary.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/photos")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping("/{entryId}")
    public ResponseEntity<Map<String, String>> upload(
            @PathVariable Long entryId,
            @RequestParam("file") MultipartFile file) throws IOException {
        String url = photoService.uploadPhoto(entryId, file);
        return ResponseEntity.ok(Map.of("url", url));
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> delete(@PathVariable Long photoId) {
        photoService.deletePhoto(photoId);
        return ResponseEntity.noContent().build();
    }
}
