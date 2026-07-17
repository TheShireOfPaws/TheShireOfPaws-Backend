package com.theshireofpaws.controller;

import com.theshireofpaws.service.interfaces.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
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

        Map<String, String> response = new HashMap<>();
        response.put("fileDownloadUri", fileUrl);
        response.put("fileType", file.getContentType());
        response.put("size", String.valueOf(file.getSize()));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteFile(@RequestParam("url") String fileUrl) {
        fileStorageService.deleteFile(fileUrl);

        Map<String, String> response = new HashMap<>();
        response.put("message", "File deleted successfully");

        return ResponseEntity.ok(response);
    }
}
