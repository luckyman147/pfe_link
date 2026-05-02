package com.pfelink.monolith.infrastructure.security.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecaptchaService {

    @Value("${RECAPTCHA_SECRET:6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe}")
    private String secret;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean verify(String token) {
        if (token == null || token.isEmpty()) return true; // TODO: make mandatory in prod
        
        try {
            Map<String, String> params = Map.of("secret", secret, "response", token);
            RecaptchaResponse response = restTemplate.postForObject(VERIFY_URL + "?secret={secret}&response={response}", null, RecaptchaResponse.class, params);
            return response != null && response.isSuccess();
        } catch (Exception e) {
            return false;
        }
    }

    @Data
    static class RecaptchaResponse {
        private boolean success;
        private String challenge_ts;
        private String hostname;
    }
}
