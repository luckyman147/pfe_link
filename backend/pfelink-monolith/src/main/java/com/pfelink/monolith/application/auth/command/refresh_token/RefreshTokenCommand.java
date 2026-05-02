package com.pfelink.monolith.application.auth.command.refresh_token;

import com.pfelink.monolith.application.auth.dto.AuthResponseDTO;
import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;
import jakarta.validation.constraints.NotBlank;

public record RefreshTokenCommand(
    @NotBlank(message = "Refresh token is required")
    String refreshToken
) implements ICommand<Result<AuthResponseDTO>> {}
