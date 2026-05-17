package com.pfelink.monolith.application.auth.dto.request.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterStudentRequest(
    @NotBlank @Email String email,
    @NotBlank String password,
    @NotBlank String fullName,
    @NotBlank String telephone,
    @NotBlank String cinNumber,
  
    String studentCardUrl,
    String draftId,
    String facultyId
) {}
