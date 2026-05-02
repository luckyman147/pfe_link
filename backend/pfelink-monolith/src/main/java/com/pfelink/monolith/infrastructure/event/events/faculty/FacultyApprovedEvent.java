package com.pfelink.monolith.infrastructure.event.events.faculty;

public record FacultyApprovedEvent(
    String facultyName,
    String facultyEmail
) {}
