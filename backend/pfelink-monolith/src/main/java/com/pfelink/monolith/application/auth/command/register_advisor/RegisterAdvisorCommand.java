package com.pfelink.monolith.application.auth.command.register_advisor;

import com.pfelink.monolith.application.auth.dto.AuthResponseDTO;
import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;

import java.util.UUID;

public record RegisterAdvisorCommand(
    String email,
    String password,
    String fullName,
    String telephone,
    String cinNumber,
    String cinCardUrl,
    String draftId,
    UUID facultyId,
    String facultyDomainEmail
) implements ICommand<Result<AuthResponseDTO>> {
    public static RegisterAdvisorCommand of(String email, String password, String fullName,
                                            String telephone, String cinNumber, String cinCardUrl,
                                            String draftId, UUID facultyId, String facultyDomainEmail) {
        return new RegisterAdvisorCommand(email, password, fullName, telephone, cinNumber,
            cinCardUrl, draftId, facultyId, facultyDomainEmail);
    }
}
