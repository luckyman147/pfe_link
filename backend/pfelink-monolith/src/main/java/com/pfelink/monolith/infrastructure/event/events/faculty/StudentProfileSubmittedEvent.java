package com.pfelink.monolith.infrastructure.event.events.faculty;

import java.util.UUID;

public record StudentProfileSubmittedEvent(
    UUID studentProfileId,
    String studentName,
    String studentEmail,
    String cinNumber,
    String studentCardUrl,
    UUID facultyId
) {
    public static StudentProfileSubmittedEvent of(UUID studentProfileId, String studentName, String studentEmail, String cinNumber, String studentCardUrl, UUID facultyId) {
        return new StudentProfileSubmittedEvent(studentProfileId, studentName, studentEmail, cinNumber, studentCardUrl, facultyId);
    }
}
