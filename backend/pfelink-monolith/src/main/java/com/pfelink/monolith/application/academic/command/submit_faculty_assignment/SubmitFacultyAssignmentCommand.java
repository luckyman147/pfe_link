package com.pfelink.monolith.application.academic.command.submit_faculty_assignment;

import com.pfelink.monolith.domain.academic.enums.faculty.AdvisorRole;
import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;

import java.util.UUID;

public record SubmitFacultyAssignmentCommand(
    UUID advisorId,
    UUID facultyId,
    AdvisorRole advisorRole,
    Integer maxCapacity,
    String facultyDomainEmail,
    String professionalProofUrl
) implements ICommand<Result<UUID>> {
    public static SubmitFacultyAssignmentCommand of(UUID advisorId, UUID facultyId, AdvisorRole advisorRole, Integer maxCapacity, String facultyDomainEmail, String professionalProofUrl) {
        return new SubmitFacultyAssignmentCommand(advisorId, facultyId, advisorRole, maxCapacity, facultyDomainEmail, professionalProofUrl);
    }
}
