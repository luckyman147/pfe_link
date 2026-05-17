package com.pfelink.monolith.application.academic.dto.response;

import com.pfelink.monolith.domain.academic.entity.faculty.Address;
import com.pfelink.monolith.domain.academic.entity.faculty.Faculty;

import java.util.UUID;

public record FacultyResponse(
    UUID id,
    String name,
    String abbreviation,
    String email,
    String websiteUrl,
    String imageUrl,
    boolean validated,
    String governorate,
    String city,
    String country
) {
    public static FacultyResponse from(Faculty f) {
        Address a = f.getAddress();
        return new FacultyResponse(
            f.getId(),
            f.getName(),
            f.getAbbreviation(),
            f.getEmail(),
            f.getWebsiteUrl(),
            f.getImageUrl(),
            f.isValidated(),
            a == null ? null : a.getGovernorate(),
            a == null ? null : a.getCity(),
            a == null ? null : a.getCountry()
        );
    }
}
