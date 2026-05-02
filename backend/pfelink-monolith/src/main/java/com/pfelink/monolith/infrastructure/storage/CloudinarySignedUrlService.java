package com.pfelink.monolith.infrastructure.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinarySignedUrlService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    public Map<String, Object> generateUploadSignature(Map<String, Object> params) {
        long timestamp = System.currentTimeMillis() / 1000L;
        
        Map<String, Object> signatureParams = new HashMap<>(params);
        signatureParams.put("timestamp", timestamp);
        
        String signature = cloudinary.apiSignRequest(signatureParams, cloudinary.config.apiSecret);
        
        Map<String, Object> result = new HashMap<>();
        result.put("signature", signature);
        result.put("timestamp", timestamp);
        result.put("api_key", apiKey);
        result.put("cloud_name", cloudName);
        
        return result;
    }
}
