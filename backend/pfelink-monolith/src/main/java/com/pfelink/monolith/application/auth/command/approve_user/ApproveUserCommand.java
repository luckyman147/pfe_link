package com.pfelink.monolith.application.auth.command.approve_user;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;

import java.util.UUID;

public record ApproveUserCommand(UUID userId) implements ICommand<Result<Void>> {}
