package com.pfelink.monolith.application.academic.query.project.get_pending_invitations;

import com.pfelink.monolith.shared.cqrs.IQuery;
import com.pfelink.monolith.shared.result.Result;
import com.pfelink.monolith.application.academic.dto.response.ProjectInvitationResponse;
import java.util.List;
import java.util.UUID;

public record GetPendingInvitationsQuery(UUID userId) implements IQuery<Result<List<ProjectInvitationResponse>>> {}
