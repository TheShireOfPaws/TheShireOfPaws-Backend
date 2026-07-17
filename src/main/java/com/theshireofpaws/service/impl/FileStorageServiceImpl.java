package com.theshireofpaws.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.theshireofpaws.exception.BadRequestException;
import com.theshireofpaws.service.interfaces.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final String UPLOAD_FOLDER = "theshireofpaws/dogs";

    private final Cloudinary cloudinary;

    public FileStorageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Failed to store empty file");
        }

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFileName);
        if (!isValidImageExtension(fileExtension)) {
            throw new BadRequestException("Only image files are allowed (jpg, jpeg, png, gif, webp)");
        }

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "folder", UPLOAD_FOLDER,
                "resource_type", "image"
            ));
            return (String) uploadResult.get("secure_url");
        } catch (IOException ex) {
            throw new RuntimeException("Could not upload file " + originalFileName + ". Please try again!", ex);
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
            String publicId = extractPublicId(fileUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException ex) {
            throw new RuntimeException("Could not delete file " + fileUrl, ex);
        }
    }

    private String extractPublicId(String fileUrl) {
        String marker = "/upload/";
        int idx = fileUrl.indexOf(marker);
        if (idx == -1) {
            return fileUrl;
        }

        String afterUpload = fileUrl.substring(idx + marker.length());
        if (afterUpload.matches("^v\\d+/.*")) {
            afterUpload = afterUpload.substring(afterUpload.indexOf('/') + 1);
        }

        int dotIdx = afterUpload.lastIndexOf('.');
        return dotIdx > -1 ? afterUpload.substring(0, dotIdx) : afterUpload;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    private boolean isValidImageExtension(String extension) {
        return extension.equals("jpg") ||
               extension.equals("jpeg") ||
               extension.equals("png") ||
               extension.equals("gif") ||
               extension.equals("webp");
    }
}
