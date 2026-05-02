package com.pfelink.monolith.application.academic.command.approve_faculty_assignment;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;

import java.util.UUID;

public record ApproveFacultyAssignmentCommand(
    UUID advisorId,
    UUID facultyId
) implements ICommand<Result<UUID>> {}
