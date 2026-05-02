package com.pfelink.monolith.application.academic.command.reject_faculty;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;

import java.util.UUID;

public record RejectFacultyCommand(
    UUID pendingFacultyId
) implements ICommand<Result<Void>> {}
