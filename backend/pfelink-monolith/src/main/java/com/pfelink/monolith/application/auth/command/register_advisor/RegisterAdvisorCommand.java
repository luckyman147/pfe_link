package com.pfelink.monolith.application.auth.command.register_advisor;

import com.pfelink.monolith.application.auth.dto.AuthResponseDTO;
import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;

public record RegisterAdvisorCommand(
    String email,
    String password,
    String fullName,
    String telephone,
    String cinNumber,
    String cinCardUrl,
    String draftId
) implements ICommand<Result<AuthResponseDTO>> {
    public static RegisterAdvisorCommand of(String email, String password, String fullName, String telephone, String cinNumber, String cinCardUrl, String draftId) {
        return new RegisterAdvisorCommand(email, password, fullName, telephone, cinNumber, cinCardUrl, draftId);
    }
}
