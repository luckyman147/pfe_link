package com.pfelink.monolith.application.auth.command.verify_otp;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;

public record VerifyOtpCommand(String email, String otpCode) implements ICommand<Result<Void>> {}
