package com.pfelink.monolith.infrastructure.event.events.auth;

public record EmailVerificationEvent(String email, String fullName, String token) {
    public static EmailVerificationEvent of(String email, String fullName, String token) {
        return new EmailVerificationEvent(email, fullName, token);
    }
}
