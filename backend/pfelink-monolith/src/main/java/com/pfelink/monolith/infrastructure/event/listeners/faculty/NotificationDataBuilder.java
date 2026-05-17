package com.pfelink.monolith.infrastructure.event.listeners.faculty;

import com.pfelink.monolith.infrastructure.email.EmailService;
import com.pfelink.monolith.infrastructure.event.events.assignment.FacultyAssignmentSubmittedEvent;
import com.pfelink.monolith.infrastructure.event.events.faculty.StudentProfileSubmittedEvent;
import org.springframework.stereotype.Component;

@Component
public class NotificationDataBuilder {

    public EmailService.AdvisorEmailData buildAdvisorEmailData(FacultyAssignmentSubmittedEvent event) {
        return new EmailService.AdvisorEmailData(
            event.advisorId().toString(),
            event.facultyId().toString(),
            event.advisorName(),
            event.advisorEmail(),
            event.advisorTelephone(),
            event.cinNumber(),
            event.role() != null ? event.role().name() : "N/A",
            event.capacity(),
            event.facultyDomainEmail(),
            event.proofUrl()
        );
    }

    public EmailService.StudentEmailData buildStudentEmailData(StudentProfileSubmittedEvent event) {
        return new EmailService.StudentEmailData(
            event.studentProfileId().toString(),
            event.studentName(),
            event.studentEmail(),
            event.cinNumber(),
            event.studentCardUrl()
        );
    }
}
