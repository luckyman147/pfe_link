package com.pfelink.monolith.infrastructure.event.listeners.faculty;

import com.pfelink.monolith.domain.academic.repository.IFacultyRepository;
import com.pfelink.monolith.domain.notification.entity.Notification;
import com.pfelink.monolith.domain.notification.enums.NotificationType;
import com.pfelink.monolith.domain.notification.repository.INotificationRepository;
import com.pfelink.monolith.infrastructure.email.EmailService;
import com.pfelink.monolith.infrastructure.event.events.assignment.FacultyAssignmentApprovedEvent;
import com.pfelink.monolith.infrastructure.event.events.assignment.FacultyAssignmentSubmittedEvent;
import com.pfelink.monolith.infrastructure.event.events.faculty.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FacultyNotificationListener {

    private final EmailService emailService;
    private final IFacultyRepository facultyRepository;
    private final INotificationRepository notificationRepository;
    private final NotificationDataBuilder dataBuilder;

    @Value("${app.admin.default-email:admin@pfelink.com}")
    private String adminEmail;

    @Async
    @EventListener
    public void onFacultySubmitted(FacultySubmittedEvent event) {
        log.info("Faculty submitted: {}", event.facultyName());
        EmailService.FacultyEmailData emailData = new EmailService.FacultyEmailData(
            event.pendingFacultyId().toString(), event.facultyName(), event.abbreviation(),
            event.email(), event.websiteUrl(), event.imageUrl(),
            event.governorate(), event.city(), event.street(),
            event.postalCode(), event.submittedByEmail()
        );
        emailService.sendFacultyApprovalEmail(adminEmail, emailData);
    }

    @Async
    @EventListener
    public void onFacultyApproved(FacultyApprovedEvent event) {
        log.info("Faculty approved: {}", event.facultyName());
        if (event.facultyEmail() != null) {
            emailService.sendFacultyApprovedEmail(event.facultyEmail(), event.facultyName());
        }
    }

    @Async
    @EventListener
    public void onFacultyRejected(FacultyRejectedEvent event) {
        log.info("Faculty rejected: {}", event.facultyName());
        if (event.facultyEmail() != null) {
            emailService.sendFacultyRejectedEmail(event.facultyEmail(), event.facultyName());
        }
    }

    @Async
    @EventListener
    public void onFacultyAssignmentSubmitted(FacultyAssignmentSubmittedEvent event) {
        log.info("Faculty assignment submitted for faculty: {}", event.facultyId());
        facultyRepository.findById(event.facultyId()).ifPresent(faculty -> {
            if (faculty.getEmail() != null) {
                EmailService.AdvisorEmailData emailData = dataBuilder.buildAdvisorEmailData(event);
                emailService.sendAdvisorApprovalEmail(faculty.getEmail(), emailData);
            }
        });
    }

    @Async
    @EventListener
    public void onFacultyAssignmentApproved(FacultyAssignmentApprovedEvent event) {
        log.info("Faculty assignment approved for advisor: {}", event.advisorEmail());
        emailService.sendAdvisorAssignmentApprovedEmail(
            event.advisorEmail(), event.advisorName(), event.facultyName(), event.seasonName()
        );
        Notification notification = new Notification();
        notification.setUserId(event.userId().toString());
        notification.setType(NotificationType.IN_APP);
        notification.setTitle("Faculty Assignment Approved");
        notification.setMessage("Your request to join " + event.facultyName() + " has been approved.");
        notificationRepository.save(notification);
    }

    @Async
    @EventListener
    public void onStudentProfileSubmitted(StudentProfileSubmittedEvent event) {
        log.info("Student profile submitted for faculty: {}", event.facultyId());
        facultyRepository.findById(event.facultyId()).ifPresent(faculty -> {
            if (faculty.getEmail() != null) {
                EmailService.StudentEmailData emailData = dataBuilder.buildStudentEmailData(event);
                emailService.sendStudentApprovalEmail(faculty.getEmail(), emailData);
            }
        });
    }

    @Async
    @EventListener
    public void onStudentProfileApproved(StudentProfileApprovedEvent event) {
        log.info("Student profile approved: {}", event.studentEmail());
        emailService.sendStudentProfileApprovedEmail(
            event.studentEmail(), event.studentName(), event.facultyName()
        );
        Notification notification = new Notification();
        notification.setUserId(event.userId().toString());
        notification.setType(NotificationType.IN_APP);
        notification.setTitle("Student Profile Approved");
        notification.setMessage("Your student profile for " + event.facultyName() + " has been approved.");
        notificationRepository.save(notification);
    }
}
