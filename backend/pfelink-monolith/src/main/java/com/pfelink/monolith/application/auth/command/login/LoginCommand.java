package com.pfelink.monolith.application.auth.command.login;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;
import com.pfelink.monolith.application.auth.dto.AuthResponseDTO;

public record LoginCommand(
    String email,
    String password
) implements ICommand<Result<AuthResponseDTO>> {}
