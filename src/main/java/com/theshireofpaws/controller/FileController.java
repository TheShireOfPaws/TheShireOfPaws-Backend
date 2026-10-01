package com.theshireofpaws.controller;

import com.theshireofpaws.service.interfaces.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileStorageService.storeFile(file);
        return ResponseEntity.ok(Map.of(
            "fileDownloadUri", fileUrl,
            "fileType", String.valueOf(file.getContentType()),
            "size", String.valueOf(file.getSize())
        ));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteFile(@RequestParam("url") String fileUrl) {
        fileStorageService.deleteFile(fileUrl);
        return ResponseEntity.ok(Map.of("message", "File deleted successfully"));
    }
}
