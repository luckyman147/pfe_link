package com.pfelink.monolith.application.academic.command.reject_faculty_assignment;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;

import java.util.UUID;

public record RejectFacultyAssignmentCommand(
    UUID advisorId,
    UUID facultyId
) implements ICommand<Result<Void>> {}
