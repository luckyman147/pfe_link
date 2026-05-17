package com.pfelink.monolith.application.auth.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record RegisterAdvisorRequest(
    @NotBlank @Email String email,
    @NotBlank String password,
    @NotBlank String fullName,
    @NotBlank String telephone,
    @NotBlank String cinNumber,
    String cinCardUrl,
    String draftId,
    UUID facultyId,
    @Email String facultyDomainEmail
) {
    @AssertTrue(message = "Either facultyId or facultyDomainEmail is required (not both)")
    public boolean isExactlyOneFacultyRefPresent() {
        boolean hasId = facultyId != null;
        boolean hasEmail = facultyDomainEmail != null && !facultyDomainEmail.isBlank();
        return hasId ^ hasEmail;
    }
}
