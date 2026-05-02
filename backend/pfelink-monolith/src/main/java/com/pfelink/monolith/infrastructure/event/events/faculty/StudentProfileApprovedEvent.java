package com.pfelink.monolith.infrastructure.event.events.faculty;

import java.util.UUID;

public record StudentProfileApprovedEvent(
    UUID userId,
    UUID studentProfileId,
    String studentName,
    String studentEmail,
    String facultyName
) {
    public static StudentProfileApprovedEvent of(UUID userId, UUID studentProfileId, String studentName, String studentEmail, String facultyName) {
        return new StudentProfileApprovedEvent(userId, studentProfileId, studentName, studentEmail, facultyName);
    }
}
