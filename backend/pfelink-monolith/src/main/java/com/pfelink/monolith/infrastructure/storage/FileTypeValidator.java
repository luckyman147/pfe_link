package com.pfelink.monolith.infrastructure.storage;

import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class FileTypeValidator {

    private static final Set<String> ALLOWED_MIME_TYPES = new HashSet<>(Arrays.asList(
        "image/jpeg", "image/png", "image/gif", "image/webp",
        "video/mp4", "video/webm", "video/ogg", "video/quicktime",
        "application/pdf", "application/msword",
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

    public void validateMimeType(String mimeType) throws FileValidationUtil.FileValidationException {
        if (mimeType == null || mimeType.isBlank()) {
            throw new FileValidationUtil.FileValidationException("File type could not be determined");
        }
        if (!ALLOWED_MIME_TYPES.contains(mimeType)) {
            throw new FileValidationUtil.FileValidationException("File type not allowed: " + mimeType);
        }
    }

    public void validateExtension(String filename) throws FileValidationUtil.FileValidationException {
        String extension = getFileExtension(filename).toLowerCase();
        if (BLOCKED_EXTENSIONS.contains(extension)) {
            throw new FileValidationUtil.FileValidationException("File type not allowed: " + extension);
        }
    }

    public void validatePathTraversal(String filename) throws FileValidationUtil.FileValidationException {
        if (PATH_TRAVERSAL_PATTERN.matcher(filename).find()) {
            throw new FileValidationUtil.FileValidationException("Invalid filename: path traversal detected");
        }
    }

    public boolean isVideoType(String mimeType) {
        return mimeType != null && (
            mimeType.startsWith("video/") ||
            mimeType.equals("video/mp4") ||
            mimeType.equals("video/webm") ||
            mimeType.equals("video/ogg") ||
            mimeType.equals("video/quicktime")
        );
    }

    private static String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
