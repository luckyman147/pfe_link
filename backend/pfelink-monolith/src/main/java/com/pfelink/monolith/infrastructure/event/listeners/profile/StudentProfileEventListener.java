package com.pfelink.monolith.infrastructure.event.listeners.profile;

import com.pfelink.monolith.domain.academic.entity.profile.StudentProfile;
import com.pfelink.monolith.domain.academic.enums.student.StudentStatus;
import com.pfelink.monolith.domain.academic.repository.IFacultyRepository;
import com.pfelink.monolith.domain.academic.repository.IStudentProfileRepository;
import com.pfelink.monolith.domain.auth.enums.UserRole;
import com.pfelink.monolith.infrastructure.event.events.auth.UserSignedUpEvent;
import com.pfelink.monolith.infrastructure.event.events.auth.UserEmailVerifiedEvent;
import com.pfelink.monolith.infrastructure.event.events.faculty.StudentProfileSubmittedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class StudentProfileEventListener {

    private final IStudentProfileRepository studentProfileRepository;
    private final IFacultyRepository facultyRepository;
    private final ApplicationEventPublisher eventPublisher;

    @EventListener
    @Transactional
    public void onUserSignedUp(UserSignedUpEvent event) {
        if (!UserRole.STUDENT.name().equals(event.role())) {
            log.info("User signed up, but not a student: {}", event.userId());
            return;
        }

        log.info("Student signed up, preparing initial profile for user: {}", event.userId());

        if (studentProfileRepository.existsByUserId(event.userId())) {
            log.warn("StudentProfile already exists for user: {}", event.userId());
            return;
        }

        StudentProfile profile = new StudentProfile();
        profile.setUserId(event.userId());
        profile.setEmail(event.email());
        profile.setFullName(event.fullName());
        profile.setCinNumber(event.cinNumber());
        profile.setStudentCardUrl(event.studentCardUrl());
        profile.setStatus(StudentStatus.PENDING);
        profile.setFacultyApproved(false);
        
        if (event.facultyId() != null && !event.facultyId().isBlank()) {
            try {
                UUID facId = UUID.fromString(event.facultyId());
                facultyRepository.findById(facId).ifPresent(profile::setFaculty);
            } catch (IllegalArgumentException e) {
                log.error("Invalid facultyId format: {}", event.facultyId());
            }
        }

        StudentProfile saved = studentProfileRepository.save(profile);
        log.info("Initial StudentProfile saved to database with ID: {}", saved.getId());
    }

    @EventListener
    @Transactional
    public void onUserEmailVerified(UserEmailVerifiedEvent event) {
        if (!UserRole.STUDENT.name().equals(event.role())) {
            return;
        }

        log.info("Email verified for student: {}. Finalizing submission to faculty.", event.email());

        studentProfileRepository.findByUserId(event.userId()).ifPresent(profile -> {
            if (profile.getFaculty() != null) {
                log.info("Notifying faculty {} about student {}", profile.getFaculty().getName(), profile.getFullName());
                eventPublisher.publishEvent(StudentProfileSubmittedEvent.of(
                    profile.getId(),
                    profile.getFullName(),
                    profile.getEmail(),
                    profile.getCinNumber(),
                    profile.getStudentCardUrl(),
                    profile.getFaculty().getId()
                ));
            } else {
                log.warn("Student verified email but has no faculty assigned: {}", profile.getId());
            }
        });
    }
}
