package com.pfelink.monolith.application.auth.dto;

import com.pfelink.monolith.domain.auth.enums.AccountStatus;
import com.pfelink.monolith.domain.auth.enums.UserRole;

import java.util.UUID;

public record AuthResponseDTO(
        UUID id,
        String email,
        String fullName,
        UserRole role,
        AccountStatus status,
        String token,
        String refreshToken
) {
    public static AuthResponseDTO of(UUID id,
                                      String email,
                                      String fullName,
                                      UserRole role,
                                      AccountStatus status,
                                      String token,
                                      String refreshToken) {
      return new AuthResponseDTO(id, email, fullName, role, status, token, refreshToken);
    }

}
