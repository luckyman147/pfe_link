package com.pfelink.monolith.infrastructure.storage;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

public interface IStorageService {
    /**
     * Uploads a file and returns a map containing the URL and public ID.
     */
    Map<String, String> upload(MultipartFile file) throws IOException;

    /**
     * Deletes a file from storage using its public ID.
     */
    void delete(String publicId) throws IOException;
}
