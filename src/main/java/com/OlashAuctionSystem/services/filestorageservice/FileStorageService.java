package com.OlashAuctionSystem.services.filestorageservice;

import com.OlashAuctionSystem.exceptions.StorageException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Component
public class FileStorageService implements IFileStorageActivities{

    @Value("$(file.upload-dir)")
    private String uploadDirPath;

    private Path uploadDir;

    @PostConstruct
    public void init() throws IOException {
        this.uploadDir = Paths.get(uploadDirPath).toAbsolutePath().normalize();
        Files.createDirectories(uploadDir);
    }

    @Override
    public String store(MultipartFile file) throws StorageException {
        String original = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String ext = "";
        int dotIndex = original.lastIndexOf(".");
        if (dotIndex >= 0){
            ext = original.substring(dotIndex);
        }

        String fileName = UUID.randomUUID() + "_" +System.currentTimeMillis() + ext;

        try {
            Path target = this.uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + fileName;
        }
        catch (IOException e){
            throw new StorageException("Failed to store file " + original, e);
        }

    }
}
