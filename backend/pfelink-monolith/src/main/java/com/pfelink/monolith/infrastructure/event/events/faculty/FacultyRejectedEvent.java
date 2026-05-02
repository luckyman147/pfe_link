package com.pfelink.monolith.infrastructure.event.events.faculty;

public record FacultyRejectedEvent(
    String facultyName,
    String facultyEmail
) {}
