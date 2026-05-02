package com.pfelink.monolith.application.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterAdvisorRequest(
    @NotBlank @Email String email,
    @NotBlank String password,
    @NotBlank String fullName,
    @NotBlank String telephone,
    @NotBlank String cinNumber,
    String cinCardUrl,
    String draftId
) {}
