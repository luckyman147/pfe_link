package com.pfelink.monolith.application.academic.command.season.activate;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;

import java.util.UUID;

public record ActivateSeasonCommand(
    UUID id
) implements ICommand<Result<Void>> {}
