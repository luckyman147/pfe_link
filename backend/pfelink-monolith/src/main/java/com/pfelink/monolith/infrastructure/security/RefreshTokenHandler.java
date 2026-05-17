package com.pfelink.monolith.infrastructure.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.Map;

/**
 * Handles token refresh against Microsoft Entra token endpoint.
 * Exchanges a refresh token for a new access token.
 */
@Component
public class RefreshTokenHandler {

    private final RestTemplate restTemplate;
    private final String clientId;
    private final String clientSecret;
    private final String tokenEndpoint;

    public RefreshTokenHandler(
        RestTemplate restTemplate,
        @Value("${ENTRA_CLIENT_ID:}") String clientId,
        @Value("${ENTRA_CLIENT_SECRET:}") String clientSecret,
        @Value("${ENTRA_TOKEN_ENDPOINT:https://ciamlogin.com/tenant-id/oauth2/v2.0/token}") String tokenEndpoint
    ) {
        this.restTemplate = restTemplate;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenEndpoint = tokenEndpoint;
    }

    /**
     * Exchange a refresh token for a new access token.
     * @param refreshToken the refresh token from the initial login
     * @return TokenResponse with new access_token and expiry
     * @throws IllegalArgumentException if refresh token is invalid
     */
    public TokenResponse refreshAccessToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("Refresh token cannot be null or empty");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "refresh_token");
            body.add("refresh_token", refreshToken);
            body.add("client_id", clientId);
            body.add("client_secret", clientSecret);
            body.add("scope", "openid profile email offline_access");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            var response = restTemplate.postForObject(tokenEndpoint, request, Map.class);

            if (response == null || !response.containsKey("access_token")) {
                throw new IllegalArgumentException("Invalid response from Microsoft token endpoint");
            }

            String accessToken = (String) response.get("access_token");
            Integer expiresIn = (Integer) response.get("expires_in");

            return new TokenResponse(accessToken, expiresIn != null ? expiresIn : 3600);
        } catch (RestClientException e) {
            throw new IllegalArgumentException("Failed to refresh token with Microsoft: " + e.getMessage(), e);
        }
    }

    /**
     * Response from token endpoint containing access token and expiry.
     */
    public record TokenResponse(String accessToken, int expiresIn) {}
}
