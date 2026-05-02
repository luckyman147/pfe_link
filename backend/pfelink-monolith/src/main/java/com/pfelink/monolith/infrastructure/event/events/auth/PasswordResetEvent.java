package com.pfelink.monolith.infrastructure.event.events.auth;

public record PasswordResetEvent(String email, String fullName, String otp) {
    public static PasswordResetEvent of(String email, String fullName, String otp) {
        return new PasswordResetEvent(email, fullName, otp);
    }
}
