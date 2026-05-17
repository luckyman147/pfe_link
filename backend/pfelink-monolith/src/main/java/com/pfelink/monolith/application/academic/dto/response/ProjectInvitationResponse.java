package com.pfelink.monolith.application.academic.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectInvitationResponse(
    UUID id,
    UUID projectId,
    String projectTitle,
    String ownerFullName,
    String ownerEmail,
    LocalDateTime createdAt
) {}
