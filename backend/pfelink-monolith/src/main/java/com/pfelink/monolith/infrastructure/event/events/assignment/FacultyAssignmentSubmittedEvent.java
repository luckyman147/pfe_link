package com.pfelink.monolith.infrastructure.event.events.assignment;

import com.pfelink.monolith.domain.academic.enums.AdvisorRole;

import java.util.UUID;

public record FacultyAssignmentSubmittedEvent(
    UUID advisorId,
    UUID facultyId,
    String advisorName,
    String advisorEmail,
    String advisorTelephone,
    String cinNumber,
    AdvisorRole role,
    Integer capacity,
    String facultyDomainEmail,
    String proofUrl
) {
    public static FacultyAssignmentSubmittedEvent of(
            UUID advisorId, UUID facultyId, String advisorName, 
            String advisorEmail, String advisorTelephone, String cinNumber,
            AdvisorRole role, Integer capacity, String facultyDomainEmail, String proofUrl) {
        return new FacultyAssignmentSubmittedEvent(
            advisorId, facultyId, advisorName, advisorEmail, 
            advisorTelephone, cinNumber, role, capacity, facultyDomainEmail, proofUrl);
    }
}
