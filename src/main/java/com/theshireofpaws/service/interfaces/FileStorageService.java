package com.theshireofpaws.service.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    
    /**
     * Store a file and return its name
     */
    String storeFile(MultipartFile file);
    
    /**
     * Load a file as Resource
     */
    Resource loadFileAsResource(String fileName);
    
    /**
     * Delete a file
     */
    void deleteFile(String fileName);
}