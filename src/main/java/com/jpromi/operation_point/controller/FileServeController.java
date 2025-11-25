package com.jpromi.operation_point.controller;

import com.jpromi.operation_point.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.URLConnection;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController("FileController")
@RequestMapping("/files")
public class FileServeController {
    private final FileStorageService fileStorageService;

    @Autowired
    public FileServeController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/{uuid}/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable UUID uuid, @PathVariable String filename) {
        File file = fileStorageService.getFile(uuid, filename);
        if (file != null && file.exists()) {
            Resource resource = new FileSystemResource(file);

            String contentType = URLConnection.guessContentTypeFromName(file.getName());
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=2592000")
                    .header(HttpHeaders.EXPIRES, ZonedDateTime.now().plusDays(30)
                            .format(DateTimeFormatter.RFC_1123_DATE_TIME))
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
