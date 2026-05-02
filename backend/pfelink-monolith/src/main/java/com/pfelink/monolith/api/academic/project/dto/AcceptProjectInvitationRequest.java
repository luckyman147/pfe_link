package com.pfelink.monolith.api.academic.project.dto;

import java.util.UUID;

public record AcceptProjectInvitationRequest(
    UUID invitationId
) {}
