package com.pfelink.monolith.application.academic.dto.response;

import java.util.UUID;

public record AdvisorProfileResponse(
    UUID id,
    UUID userId,
    String fullName,
    String email,
    String telephone,
    String department, // From role or specialized field if added
    String specialization
) {}
