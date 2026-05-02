package com.pfelink.monolith.application.academic.command.project.create;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;
import java.util.UUID;

public record CreateProjectCommand(
    UUID studentUserId,
    String title,
    String description
) implements ICommand<Result<UUID>> {}
