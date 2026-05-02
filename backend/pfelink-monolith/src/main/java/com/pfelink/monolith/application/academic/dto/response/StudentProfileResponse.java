package com.pfelink.monolith.application.academic.dto.response;

import com.pfelink.monolith.domain.academic.enums.StudentStatus;
import java.util.UUID;

public record StudentProfileResponse(
    UUID id,
    UUID userId,
    String fullName,
    String email,
    StudentStatus status,
    UUID facultyId
) {}
