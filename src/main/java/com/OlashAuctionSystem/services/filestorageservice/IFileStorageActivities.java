package com.OlashAuctionSystem.services.filestorageservice;

import com.OlashAuctionSystem.exceptions.StorageException;
import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageActivities {
    String store(MultipartFile file) throws StorageException;
}
