package com.pfelink.monolith.application.academic.command.approve_faculty;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;

import java.util.UUID;

public record ApproveFacultyCommand(
    UUID pendingFacultyId
) implements ICommand<Result<UUID>> {}
