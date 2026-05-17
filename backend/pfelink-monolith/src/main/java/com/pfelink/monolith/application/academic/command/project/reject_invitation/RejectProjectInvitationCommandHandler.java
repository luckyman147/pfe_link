package com.pfelink.monolith.application.academic.command.project.reject_invitation;

import com.pfelink.monolith.domain.academic.entity.project.ProjectInvitation;
import com.pfelink.monolith.domain.academic.enums.ProjectInvitationStatus;
import com.pfelink.monolith.domain.academic.repository.IProjectInvitationRepository;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RejectProjectInvitationCommandHandler implements ICommandHandler<RejectProjectInvitationCommand, Result<Void>> {

    private final IProjectInvitationRepository invitationRepository;

    @Override
    @Transactional
    public Result<Void> handle(RejectProjectInvitationCommand cmd) {
        ProjectInvitation invitation = invitationRepository.findById(cmd.invitationId()).orElse(null);
        if (invitation == null) {
            return Result.failure(Error.failure("Invitation.NotFound", "Invitation not found"));
        }

        if (invitation.getStatus() != ProjectInvitationStatus.PENDING) {
            return Result.failure(Error.failure("Invitation.InvalidStatus", "Invitation is already " + invitation.getStatus()));
        }

        UUID currentUserId = com.pfelink.monolith.infrastructure.security.util.SecurityUtils.getCurrentUserId();
        if (!invitation.getInvitee().getUserId().equals(currentUserId)) {
            return Result.failure(Error.failure("Invitation.Unauthorized", "This invitation was not sent to you"));
        }

        invitation.setStatus(ProjectInvitationStatus.REJECTED);
        invitation.setRespondedAt(LocalDateTime.now());
        invitationRepository.save(invitation);

        return Result.success(null);
    }
}
