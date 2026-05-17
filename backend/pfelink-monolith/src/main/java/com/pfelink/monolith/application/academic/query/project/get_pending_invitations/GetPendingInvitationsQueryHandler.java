package com.pfelink.monolith.application.academic.query.project.get_pending_invitations;

import com.pfelink.monolith.application.academic.dto.response.ProjectInvitationResponse;
import com.pfelink.monolith.domain.academic.entity.project.ProjectInvitation;
import com.pfelink.monolith.domain.academic.enums.ProjectInvitationStatus;
import com.pfelink.monolith.domain.academic.repository.IProjectInvitationRepository;
import com.pfelink.monolith.shared.cqrs.IQueryHandler;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPendingInvitationsQueryHandler implements IQueryHandler<GetPendingInvitationsQuery, Result<List<ProjectInvitationResponse>>> {

    private final IProjectInvitationRepository invitationRepository;

    @Override
    public Result<List<ProjectInvitationResponse>> handle(GetPendingInvitationsQuery query) {
        List<ProjectInvitation> invitations = invitationRepository.findByInviteeUserIdAndStatus(
            query.userId(),
            ProjectInvitationStatus.PENDING
        );

        List<ProjectInvitationResponse> responses = invitations.stream()
            .map(invitation -> new ProjectInvitationResponse(
                invitation.getId(),
                invitation.getProject().getId(),
                invitation.getProject().getTitle(),
                invitation.getProject().getOwner().getFullName(),
                invitation.getProject().getOwner().getEmail(),
                invitation.getCreatedAt()
            ))
            .toList();

        return Result.success(responses);
    }
}
