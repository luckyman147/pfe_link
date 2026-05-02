package com.pfelink.monolith.application.auth.dto;

import com.pfelink.monolith.domain.auth.enums.AccountStatus;
import com.pfelink.monolith.domain.auth.enums.UserRole;

import java.util.UUID;

public record UserDTO(
    UUID id,
    String email,
    String fullName,
    UserRole role,
    AccountStatus status,
    boolean emailVerified,
    String imageUrl,
    String telephone,
    StudentProfileDTO studentProfile,
    AdvisorProfileDTO advisorProfile
) {
    public record StudentProfileDTO(
        UUID id,
        String fullName,
        String studentCardUrl,
        String facultyName,
        String facultyLocation,
        String verificationStatus
    ) {}

    public record AdvisorProfileDTO(
        UUID id,
        String fullName,
        String telephone,
        String specialization,
        String department
    ) {}
}
