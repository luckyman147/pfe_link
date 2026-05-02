package com.pfelink.monolith.application.auth.command.forgot_password;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;

public record ForgotPasswordCommand(String email) implements ICommand<Result<Void>> {}
