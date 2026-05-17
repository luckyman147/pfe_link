package com.pfelink.monolith.infrastructure.event.listeners.auth;

import com.pfelink.monolith.infrastructure.email.EmailService;
import com.pfelink.monolith.infrastructure.event.events.auth.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthNotificationListener {

    private final EmailService emailService;

    @Async
    @EventListener
    public void onUserSignedUp(UserSignedUpEvent event) {
        log.info("User signed up: {}", event.email());
        emailService.sendWelcomeEmail(event.email(), event.fullName());
    }

    @Async
    @EventListener
    public void onEmailVerificationRequested(EmailVerificationEvent event) {
        log.info("Email verification requested: {}", event.email());
        emailService.sendVerificationEmail(event.email(), event.fullName(), event.token());
    }

    @Async
    @EventListener
    public void onPasswordResetRequested(PasswordResetEvent event) {
        log.info("Password reset OTP requested: {}", event.email());
        emailService.sendPasswordResetOtp(event.email(), event.fullName(), event.otp());
    }

    @Async
    @EventListener
    public void onUserEmailVerified(UserEmailVerifiedEvent event) {
        log.info("User email verified: {} ({})", event.email(), event.role());
        // Detailed faculty joining notifications are now handled by specialized listeners 
        // linked to the student/advisor profiles.
    }
}
