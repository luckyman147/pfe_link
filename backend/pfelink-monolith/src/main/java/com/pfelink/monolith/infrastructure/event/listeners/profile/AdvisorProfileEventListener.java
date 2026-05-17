package com.pfelink.monolith.infrastructure.event.listeners.profile;

import com.pfelink.monolith.domain.academic.entity.profile.AdvisorProfile;
import com.pfelink.monolith.domain.academic.repository.IAdvisorProfileRepository;
import com.pfelink.monolith.domain.auth.enums.UserRole;
import com.pfelink.monolith.domain.auth.repository.IUserRepository;
import com.pfelink.monolith.infrastructure.event.events.auth.UserEmailVerifiedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdvisorProfileEventListener {

    private final IAdvisorProfileRepository advisorProfileRepository;
    private final IUserRepository userRepository;

    @EventListener
    @Transactional
    public void onUserSignedUp(com.pfelink.monolith.infrastructure.event.events.auth.UserSignedUpEvent event) {
        if (!UserRole.ADVISOR.name().equals(event.role())) {
            return;
        }

        log.info("Creating initial AdvisorProfile for advisor: {}", event.userId());

        if (advisorProfileRepository.findByUserId(event.userId()).isPresent()) {
            log.warn("AdvisorProfile already exists for user: {}", event.userId());
            return;
        }

        AdvisorProfile profile = new AdvisorProfile();
        profile.setUserId(event.userId());
        profile.setEmail(event.email());
        profile.setFullName(event.fullName());
        // For telephone, we still need it from the user if it's there, but event doesn't have it yet.
        // Actually, telephone is still in User entity, so we can fetch user.
        userRepository.findById(event.userId()).ifPresent(user -> {
            profile.setTelephone(user.getTelephone());
        });
        
        profile.setCinNumber(event.cinNumber());
        profile.setCinScreenshotUrl(event.cinCardUrl());
        profile.setPendingFacultyId(event.facultyId() == null ? null : UUID.fromString(event.facultyId()));
        profile.setPendingFacultyDomainEmail(event.pendingFacultyDomainEmail());

        advisorProfileRepository.save(profile);
        log.info("Initial AdvisorProfile created successfully for user: {}", event.userId());
    }

    @EventListener
    @Transactional
    public void onUserEmailVerified(UserEmailVerifiedEvent event) {
        if (!UserRole.ADVISOR.name().equals(event.role())) {
            return;
        }
        log.info("Advisor email verified: {}", event.userId());
        // Profile already created at signup
    }
}
