package com.pfelink.monolith.infrastructure.storage;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class FileValidationUtil {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB default
    private static final long MAX_VIDEO_SIZE = 50 * 1024 * 1024; // 50MB for videos
    private static final Set<String> ALLOWED_MIME_TYPES = new HashSet<>(Arrays.asList(
        "image/jpeg",
        "image/png",
        "image/gif",
        "image/webp",
        "video/mp4",
        "video/webm",
        "video/ogg",
        "video/quicktime",
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/vnd.ms-excel",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    ));

    private static final Set<String> BLOCKED_EXTENSIONS = new HashSet<>(Arrays.asList(
        "exe", "bat", "cmd", "sh", "bash", "jsp", "jspx", "jsw", "jsv", "jtml",
        "jar", "zip", "rar", "7z", "tar", "gz", "iso", "dmg", "msi", "scr",
        "vbs", "js", "py", "php", "asp", "aspx", "cgi", "pl", "app", "deb",
        "rpm", "apk", "so", "dll", "sys", "bin", "com", "mach", "o"
    ));

    private static final Pattern PATH_TRAVERSAL_PATTERN = Pattern.compile("\\.\\.|\\/|\\\\");

    public static void validateFile(MultipartFile file) throws FileValidationException {
        if (file == null || file.isEmpty()) {
            throw new FileValidationException("File is empty");
        }

        validateFileName(file.getOriginalFilename());
        validateFileSize(file.getSize(), file.getContentType());
        validateMimeType(file.getContentType());
    }

    private static void validateFileName(String filename) throws FileValidationException {
        if (filename == null || filename.isBlank()) {
            throw new FileValidationException("Filename is required");
        }

        // Check for path traversal
        if (PATH_TRAVERSAL_PATTERN.matcher(filename).find()) {
            throw new FileValidationException("Invalid filename: path traversal detected");
        }

        // Check file extension
        String extension = getFileExtension(filename).toLowerCase();
        if (BLOCKED_EXTENSIONS.contains(extension)) {
            throw new FileValidationException("File type not allowed: " + extension);
        }

        // Filename should be alphanumeric with dots, hyphens, underscores
        if (!filename.matches("^[a-zA-Z0-9._-]+$")) {
            throw new FileValidationException("Filename contains invalid characters");
        }
    }

    private static void validateFileSize(long fileSize, String mimeType) throws FileValidationException {
        if (fileSize == 0) {
            throw new FileValidationException("File cannot be empty");
        }

        long maxSize = isVideoType(mimeType) ? MAX_VIDEO_SIZE : MAX_FILE_SIZE;
        if (fileSize > maxSize) {
            throw new FileValidationException(
                String.format("File size exceeds maximum allowed: %dMB", maxSize / (1024 * 1024))
            );
        }
    }

    private static boolean isVideoType(String mimeType) {
        return mimeType != null && (
            mimeType.startsWith("video/") ||
            mimeType.equals("video/mp4") ||
            mimeType.equals("video/webm") ||
            mimeType.equals("video/ogg") ||
            mimeType.equals("video/quicktime")
        );
    }

    private static void validateMimeType(String mimeType) throws FileValidationException {
        if (mimeType == null || mimeType.isBlank()) {
            throw new FileValidationException("File type could not be determined");
        }

        if (!ALLOWED_MIME_TYPES.contains(mimeType)) {
            throw new FileValidationException("File type not allowed: " + mimeType);
        }
    }

    public static String sanitizeFilename(String filename) {
        if (filename == null) {
            return "file_" + System.currentTimeMillis();
        }

        // Replace spaces and special characters
        String sanitized = filename.replaceAll("[^a-zA-Z0-9._-]", "_");

        // Remove multiple underscores
        sanitized = sanitized.replaceAll("_+", "_");

        // Limit length to 255 characters
        if (sanitized.length() > 255) {
            sanitized = sanitized.substring(0, 255);
        }

        return sanitized;
    }

    private static String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    public static class FileValidationException extends Exception {
        public FileValidationException(String message) {
            super(message);
        }
    }
}
