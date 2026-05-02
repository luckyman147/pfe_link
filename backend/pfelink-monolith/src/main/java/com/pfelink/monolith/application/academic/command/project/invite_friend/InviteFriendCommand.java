package com.pfelink.monolith.application.academic.command.project.invite_friend;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;
import java.util.UUID;

public record InviteFriendCommand(
    UUID ownerId,
    UUID projectId,
    UUID inviteeStudentProfileId
) implements ICommand<Result<UUID>> {}

