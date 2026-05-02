package com.pfelink.monolith.infrastructure.storage.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class FileSignatureValidator {

    private static final Map<String, String> SIGNATURES = new HashMap<>();

    static {
        // PDF: %PDF
        SIGNATURES.put("application/pdf", "25504446");
        // JPEG: FF D8 FF
        SIGNATURES.put("image/jpeg", "FFD8FF");
        // PNG: 89 50 4E 47 0D 0A 1A 0A
        SIGNATURES.put("image/png", "89504E470D0A1A0A");
    }

    public boolean isValid(InputStream inputStream, String contentType) throws IOException {
        String expectedSignature = SIGNATURES.get(contentType);
        if (expectedSignature == null) {
            log.warn("No signature defined for content type: {}", contentType);
            return true; // Allow if not specified for now, or change to false for stricter security
        }

        byte[] bytes = new byte[expectedSignature.length() / 2];
        int read = inputStream.read(bytes);
        if (read == -1) return false;

        String actualSignature = bytesToHex(bytes).toUpperCase();
        boolean valid = actualSignature.startsWith(expectedSignature);
        
        if (!valid) {
            log.error("File signature mismatch! Expected: {}, Actual: {}", expectedSignature, actualSignature);
        }
        
        return valid;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
