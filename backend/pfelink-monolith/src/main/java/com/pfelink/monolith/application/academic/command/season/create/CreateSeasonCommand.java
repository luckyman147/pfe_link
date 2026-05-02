package com.pfelink.monolith.application.academic.command.season.create;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;

import java.time.LocalDate;
import java.util.UUID;

public record CreateSeasonCommand(
    String name,
    LocalDate startDate,
    LocalDate endDate,
    boolean isActive
) implements ICommand<Result<UUID>> {}
