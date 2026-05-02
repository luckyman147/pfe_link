package com.pfelink.monolith.application.academic.command.project.accept_invitation;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;
import java.util.UUID;

public record AcceptProjectInvitationCommand(
    UUID invitationId
) implements ICommand<Result<Void>> {}
