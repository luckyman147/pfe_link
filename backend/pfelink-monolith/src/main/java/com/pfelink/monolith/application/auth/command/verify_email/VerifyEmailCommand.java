package com.pfelink.monolith.application.auth.command.verify_email;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;

public record VerifyEmailCommand(String token) implements ICommand<Result<Boolean>> {}
