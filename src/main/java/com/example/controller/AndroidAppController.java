package com.example.controller;

import com.example.model.AndroidApp;
import com.example.service.AndroidAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class AndroidAppController {
    private static final Logger logger = LoggerFactory.getLogger(AndroidAppController.class);

    private final AndroidAppService androidAppService;

    @Autowired
    public AndroidAppController(AndroidAppService androidAppService) {
        this.androidAppService = androidAppService;
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        AndroidApp androidApp = androidAppService.storeFile(file);
        return ResponseEntity.ok().body(androidApp.getAppLabel() + " uploaded successfully!");
    }

    @PostMapping("/uploadMultipleFiles")
    public List<String> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.stream(files)
                .map(file -> uploadFile(file).getBody())
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        // Load file from database
        AndroidApp androidApp = androidAppService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(androidApp.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + androidApp.getAppLabel() + "\"")
                .body(new ByteArrayResource(androidApp.getData()));
    }
}
