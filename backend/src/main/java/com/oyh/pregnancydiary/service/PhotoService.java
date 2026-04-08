package com.oyh.pregnancydiary.service;

import com.oyh.pregnancydiary.entity.DiaryEntry;
import com.oyh.pregnancydiary.entity.DiaryPhoto;
import com.oyh.pregnancydiary.repository.DiaryEntryRepository;
import com.oyh.pregnancydiary.repository.DiaryPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final DiaryPhotoRepository diaryPhotoRepository;
    private final DiaryEntryRepository diaryEntryRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String uploadPhoto(Long entryId, MultipartFile file) throws IOException {
        DiaryEntry entry = diaryEntryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("일기를 찾을 수 없습니다."));

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path dest = uploadPath.resolve(filename);

        Files.copy(file.getInputStream(),dest, StandardCopyOption.REPLACE_EXISTING);

        DiaryPhoto photo = DiaryPhoto.builder()
                .diaryEntry(entry)
                .filePath(filename)
                .build();
        diaryPhotoRepository.save(photo);

        return "/uploads/" + filename;
    }


    public void deletePhoto(Long photoId) {
        DiaryPhoto photo = diaryPhotoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("사진을 찾을 수 없습니다."));

        File file = new File(uploadDir + "/" + photo.getFilePath());
        if (file.exists()) file.delete();

        diaryPhotoRepository.deleteById(photoId);
    }
}
