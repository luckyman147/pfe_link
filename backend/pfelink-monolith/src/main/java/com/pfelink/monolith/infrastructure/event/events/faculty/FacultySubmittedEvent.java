package com.pfelink.monolith.infrastructure.event.events.faculty;

import java.util.UUID;

public record FacultySubmittedEvent(
    UUID pendingFacultyId,
    String facultyName,
    String abbreviation,
    String email,
    String websiteUrl,
    String imageUrl,
    String governorate,
    String city,
    String street,
    String postalCode,
    String submittedByEmail
) {
    public static FacultySubmittedEvent of(UUID pendingFacultyId, String facultyName, String abbreviation, String email, String websiteUrl, String imageUrl, String governorate, String city, String street, String postalCode, String submittedByEmail) {
        return new FacultySubmittedEvent(pendingFacultyId, facultyName, abbreviation, email, websiteUrl, imageUrl, governorate, city, street, postalCode, submittedByEmail);
    }
}
