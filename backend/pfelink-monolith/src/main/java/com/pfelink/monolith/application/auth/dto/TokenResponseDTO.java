package com.pfelink.monolith.application.auth.dto;

public record TokenResponseDTO(
        String accessToken,
        String refreshToken
) {
    public static TokenResponseDTO of(String accessToken, String refreshToken) {
        return new TokenResponseDTO(accessToken, refreshToken);
    }
}
