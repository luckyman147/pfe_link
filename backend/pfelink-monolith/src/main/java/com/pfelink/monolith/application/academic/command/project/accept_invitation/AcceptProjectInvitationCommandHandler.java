package com.pfelink.monolith.application.academic.command.project.accept_invitation;

import com.pfelink.monolith.domain.academic.entity.profile.StudentProfile;
import com.pfelink.monolith.domain.academic.entity.project.ProjectInvitation;
import com.pfelink.monolith.domain.academic.enums.ProjectInvitationStatus;
import com.pfelink.monolith.domain.academic.repository.IProjectInvitationRepository;
import com.pfelink.monolith.domain.academic.repository.IStudentProfileRepository;
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
public class AcceptProjectInvitationCommandHandler implements ICommandHandler<AcceptProjectInvitationCommand, Result<Void>> {

    private final IProjectInvitationRepository invitationRepository;
    private final IStudentProfileRepository studentProfileRepository;

    @Override
    @Transactional
    public Result<Void> handle(AcceptProjectInvitationCommand cmd) {
        ProjectInvitation invitation = invitationRepository.findById(cmd.invitationId()).orElse(null);
        if (invitation == null) {
            return Result.failure(Error.failure("Invitation.NotFound", "Invitation not found"));
        }

        if (invitation.getStatus() != ProjectInvitationStatus.PENDING) {
            return Result.failure(Error.failure("Invitation.InvalidStatus", "Invitation is already " + invitation.getStatus()));
        }

        StudentProfile invitee = invitation.getInvitee();
        UUID currentUserId = com.pfelink.monolith.infrastructure.security.util.SecurityUtils.getCurrentUserId();
        
        if (!invitee.getUserId().equals(currentUserId)) {
            return Result.failure(Error.failure("Invitation.Unauthorized", "This invitation was not sent to you"));
        }

        if (invitee.getProject() != null) {
            return Result.failure(Error.failure("Student.HasProject", "You already belong to a project"));
        }


        invitation.setStatus(ProjectInvitationStatus.ACCEPTED);
        invitation.setRespondedAt(LocalDateTime.now());
        invitationRepository.save(invitation);

        invitee.setProject(invitation.getProject());
        studentProfileRepository.save(invitee);

        return Result.success(null);
    }
}
