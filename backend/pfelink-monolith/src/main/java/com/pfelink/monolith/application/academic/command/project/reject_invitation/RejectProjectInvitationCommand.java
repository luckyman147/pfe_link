package com.pfelink.monolith.application.academic.command.project.reject_invitation;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;
import java.util.UUID;

public record RejectProjectInvitationCommand(UUID invitationId) implements ICommand<Result<Void>> {}
