package com.pfelink.monolith.application.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RefreshTokenRequest(
    @NotBlank(message = "Refresh token is required")
    @Size(min = 100, max = 2000, message = "Refresh token format is invalid")
    String refreshToken
) {}
