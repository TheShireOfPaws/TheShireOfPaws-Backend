package com.theshireofpaws.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String storeFile(MultipartFile file);

    void deleteFile(String fileUrl);
}
