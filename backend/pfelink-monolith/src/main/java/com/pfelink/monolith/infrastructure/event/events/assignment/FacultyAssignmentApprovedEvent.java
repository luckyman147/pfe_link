package com.pfelink.monolith.infrastructure.event.events.assignment;

import java.time.LocalDateTime;
import java.util.UUID;

public record FacultyAssignmentApprovedEvent(
    LocalDateTime timestamp,
    UUID userId,
    String advisorEmail,
    String advisorName,
    String facultyName,
    String seasonName
) {
    public static FacultyAssignmentApprovedEvent of(LocalDateTime timestamp, UUID userId, String advisorEmail, String advisorName, String facultyName, String seasonName) {
        return new FacultyAssignmentApprovedEvent(timestamp, userId, advisorEmail, advisorName, facultyName, seasonName);
    }
}
