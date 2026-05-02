package com.pfelink.monolith.infrastructure.event.events.auth;

import java.util.UUID;

public record UserSignedUpEvent(
    UUID userId,
    String email,
    String fullName,
    String role,
    String cinNumber,
    String cinCardUrl,
    String studentCardUrl,
    String facultyId
) {
    public static UserSignedUpEvent of(UUID userId, String email, String fullName, String role, String cinNumber, String cinCardUrl, String studentCardUrl, String facultyId) {
        return new UserSignedUpEvent(userId, email, fullName, role, cinNumber, cinCardUrl, studentCardUrl, facultyId);
    }
}
