package com.pfelink.monolith.application.academic.dto.response;

import java.util.UUID;

import com.pfelink.monolith.domain.academic.enums.student.StudentStatus;

public record StudentProfileResponse(
    UUID id,
    UUID userId,
    String fullName,
    String email,
    StudentStatus status,
    UUID facultyId
) {}
