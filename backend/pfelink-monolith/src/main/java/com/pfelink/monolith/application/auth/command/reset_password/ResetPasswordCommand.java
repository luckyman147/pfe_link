package com.pfelink.monolith.application.auth.command.reset_password;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;

public record ResetPasswordCommand(
    String email,
    String otp,
    String newPassword
) implements ICommand<Result<Void>> {}
