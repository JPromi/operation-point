package com.jpromi.operation_point.service;

import com.jpromi.operation_point.enitiy.FileData;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

public interface FileStorageService {
    String getExternalUrl(FileData data);
    String getExternalUrl(FileData data, String fallbackFileName);
    File getFile(UUID uuid, String filename);
    File getFile(FileData data);
    FileData saveFile(File file);
    FileData saveFile(MultipartFile file);
    Void deleteFile(FileData data);
}
