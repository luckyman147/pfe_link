package com.pfelink.monolith.infrastructure.event.events.auth;

import java.util.UUID;

public record UserEmailVerifiedEvent(
    UUID userId,
    String email,
    String fullName,
    String role
) {
    public static UserEmailVerifiedEvent of(UUID userId, String email, String fullName, String role) {
        return new UserEmailVerifiedEvent(userId, email, fullName, role);
    }
}
