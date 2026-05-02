package com.pfelink.monolith.infrastructure.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryStorageService implements IStorageService {

    private final Cloudinary cloudinary;

    @Override
    public Map<String, String> upload(MultipartFile file) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        Map<String, String> result = new HashMap<>();
        result.put("url", (String) uploadResult.get("secure_url"));
        result.put("public_id", (String) uploadResult.get("public_id"));
        return result;
    }

    @Override
    public void delete(String publicId) throws IOException {
        if (publicId != null && !publicId.isEmpty()) {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }
    }
}
