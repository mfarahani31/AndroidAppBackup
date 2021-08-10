package com.example.service;

import com.example.exception.FileStorageException;
import com.example.exception.MyFileNotFoundException;
import com.example.model.AndroidApp;
import com.example.repository.AndroidAppRepository;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class AndroidAppService {
    private final AndroidAppRepository androidAppRepository;

    @Autowired
    public AndroidAppService(AndroidAppRepository androidAppRepository) {
        this.androidAppRepository = androidAppRepository;
    }

    @Value("${app.upload.dir:${user.home}}")
    public String uploadDir;


    public AndroidApp storeFile(MultipartFile file) {
        // Normalize file name
        var fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        ApkMeta apkMeta;
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            var copyLocation = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));

            Files.copy(file.getInputStream(), copyLocation);

            try (var apkFile = new ApkFile(
                    Paths.get(uploadDir + File.separator + StringUtils.cleanPath(file.getOriginalFilename())).toString())) {
                apkMeta = apkFile.getApkMeta();
            }

            var androidApp = new AndroidApp(
                    fileName,
                    apkMeta.getVersionName(),
                    apkMeta.getVersionCode().toString(),
                    copyLocation.toString(),
                    file.getContentType(),
                    file.getBytes());
            return androidAppRepository.save(androidApp);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public AndroidApp getFile(String fileId) {
        return androidAppRepository.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
    }
}
