package com.pfelink.monolith.application.auth.command.refresh_token;

import com.pfelink.monolith.application.auth.dto.TokenResponseDTO;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenCommandHandler
        implements ICommandHandler<RefreshTokenCommand, Result<TokenResponseDTO>> {

    @Override
    public Result<TokenResponseDTO> handle(RefreshTokenCommand cmd) {
        return Result.failure(Error.failure(
            "Auth.LegacyJWTMigration",
            "Custom JWT token refresh has been removed. Please use Microsoft Entra External ID OAuth2 token refresh instead."
        ));
    }
}
