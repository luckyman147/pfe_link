package com.pfelink.monolith.infrastructure.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class FileValidationUtil {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final long MAX_VIDEO_SIZE = 50 * 1024 * 1024;

    private final FileTypeValidator typeValidator;

    public void validateFile(MultipartFile file) throws FileValidationException {
        if (file == null || file.isEmpty()) {
            throw new FileValidationException("File is empty");
        }
        validateFileName(file.getOriginalFilename());
        validateFileSize(file.getSize(), file.getContentType());
        typeValidator.validateMimeType(file.getContentType());
    }

    private void validateFileName(String filename) throws FileValidationException {
        if (filename == null || filename.isBlank()) {
            throw new FileValidationException("Filename is required");
        }
        typeValidator.validatePathTraversal(filename);
        typeValidator.validateExtension(filename);
        if (!filename.matches("^[a-zA-Z0-9._-]+$")) {
            throw new FileValidationException("Filename contains invalid characters");
        }
    }

    private void validateFileSize(long fileSize, String mimeType) throws FileValidationException {
        if (fileSize == 0) {
            throw new FileValidationException("File cannot be empty");
        }
        long maxSize = typeValidator.isVideoType(mimeType) ? MAX_VIDEO_SIZE : MAX_FILE_SIZE;
        if (fileSize > maxSize) {
            throw new FileValidationException(
                String.format("File size exceeds maximum allowed: %dMB", maxSize / (1024 * 1024))
            );
        }
    }

    public static String sanitizeFilename(String filename) {
        if (filename == null) {
            return "file_" + System.currentTimeMillis();
        }
        String sanitized = filename.replaceAll("[^a-zA-Z0-9._-]", "_");
        sanitized = sanitized.replaceAll("_+", "_");
        if (sanitized.length() > 255) {
            sanitized = sanitized.substring(0, 255);
        }
        return sanitized;
    }

    public static class FileValidationException extends Exception {
        public FileValidationException(String message) {
            super(message);
        }
    }
}
