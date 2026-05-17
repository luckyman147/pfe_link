package com.pfelink.monolith.application.auth.command.login;

import com.pfelink.monolith.application.auth.dto.AuthResponseDTO;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import org.springframework.stereotype.Service;

@Service
public class LoginCommandHandler
        implements ICommandHandler<LoginCommand, Result<AuthResponseDTO>> {

    @Override
    public Result<AuthResponseDTO> handle(LoginCommand cmd) {
        return Result.failure(Error.failure(
            "Auth.LegacyJWTMigration",
            "Custom JWT authentication has been removed. Please authenticate via Microsoft Entra External ID using OAuth2."
        ));
    }
}
