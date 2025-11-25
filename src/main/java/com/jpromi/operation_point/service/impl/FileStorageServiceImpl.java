package com.jpromi.operation_point.service.impl;

import com.jpromi.operation_point.entity.FileData;
import com.jpromi.operation_point.repository.FileDataRepository;
import com.jpromi.operation_point.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${com.jpromi.operation_point.file.storage.path}")
    private String fileStoragePath;

    @Value("${com.jpromi.operation_point.domain}")
    private String host;

    @Autowired
    private FileDataRepository fileDataRepository;

    @Override
    public String getExternalUrl(FileData data) {
        if (data == null) {
            return null;
        } else {
            return host + "/files/" + data.getUuid() + "/" +  data.getFileName();
        }
    }

    @Override
    public String getExternalUrl(FileData data, String fallbackFileName) {
        String fileName = getExternalUrl(data);

        if (fileName == null && fallbackFileName != null) {
            fileName = host + fallbackFileName;
        }

        return fileName;
    }

    @Override
    public File getFile(UUID uuid, String filename) {
        Optional<FileData> fileData = fileDataRepository.findByFileNameAndUuid(filename, uuid);

        return getFile(fileData.get());
    }

    @Override
    public File getFile(FileData data) {
        if(data != null) {
            return _getFile(data.getId(), data.getFileName());
        } else {
            return null;
        }
    }

    @Override
    public FileData saveFile(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String safeFileName = (originalFilename != null ? originalFilename : "file");

            Path targetPath = Paths.get(fileStoragePath).resolve(safeFileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return saveFile(targetPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Could not store file.", e);
        }
    }

    @Override
    public FileData saveFile(File file) {
        try {
            FileData fileData = FileData.builder().build();
            fileData.setFileName(file.getName());
            fileData.setFileSize(file.length());
            fileData.setContentType(detectContentType(file));
            fileData = fileDataRepository.save(fileData);

            String path = _storeFile(file, fileData.getId(), file.getName());
            if (path != null) {
                fileData = fileDataRepository.save(fileData);
                return fileData;
            } else {
                fileDataRepository.delete(fileData);
                throw new RuntimeException("Could not store file.");
            }
        } catch (SecurityException e) {
            throw new RuntimeException("Could not store file.", e);
        }
    }

    @Override
    public Void deleteFile(FileData data) {
        if(data != null) {
            _deleteFile(data.getId(), data.getFileName());
            fileDataRepository.delete(data);
        }
        return null;
    }

    private File _getFile(Long id, String filename) {
        Path path = Path.of(fileStoragePath + "/" + id + "/" + filename);

        if (Files.exists(path)) {
            return path.toFile();
        } else {
            return null;
        }
    }

    private String _storeFile(File file, Long id, String filename) {
        try {
            Path path = Path.of(fileStoragePath);
            Path pathId = path.resolve(id.toString());

            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            if (!Files.exists(pathId)) {
                Files.createDirectories(pathId);
            }

            Path targetPath = pathId.resolve(filename);

            Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return "/" + path.relativize(targetPath).toString().replace("\\", "/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Void _deleteFile(Long id, String filename) {
        try {
            Path path = Path.of(fileStoragePath);
            Path pathId = path.resolve(id.toString());
            Path targetPath = pathId.resolve(filename);

            System.out.println(targetPath.toString());

            if (Files.exists(targetPath)) {
                Files.delete(targetPath);
            }

            if (Files.exists(pathId) && Files.list(pathId).findAny().isEmpty()) {
                Files.delete(pathId);
            }

            return null;
        } catch (IOException e) {
            // throw new RuntimeException(e);
            return null;
        }
    }

    private String detectContentType(File file) {
        try {
            String contentType = Files.probeContentType(file.toPath());
            return contentType != null ? contentType : "application/octet-stream";
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }
}
