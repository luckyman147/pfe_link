package com.pfelink.monolith.application.academic.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateFacultyRequest(
    @NotBlank String name,

    @NotBlank String abbreviation,

    @Email String email,

    String websiteUrl,

    String imageUrl,
    String adminPassword,
    
    String path
) {}
