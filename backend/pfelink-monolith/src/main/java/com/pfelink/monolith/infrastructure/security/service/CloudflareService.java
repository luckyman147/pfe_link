package com.pfelink.monolith.infrastructure.security.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudflareService {

    @Value("${CLOUDFLARE_TURNSTILE_SECRET:}")
    private String secretKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String VERIFY_URL = "https://challenges.cloudflare.com/turnstile/v0/siteverify";

    public boolean verify(String token) {
        if (token == null || token.isEmpty()) return true;

        try {
            Map<String, String> request = Map.of("secret", secretKey, "response", token);
            TurnstileResponse response = restTemplate.postForObject(
                VERIFY_URL,
                request,
                TurnstileResponse.class
            );
            return response != null && response.isSuccess();
        } catch (Exception e) {
            return false;
        }
    }

    @Data
    static class TurnstileResponse {
        private boolean success;
        private String challenge_ts;
        private String hostname;
    }
}
