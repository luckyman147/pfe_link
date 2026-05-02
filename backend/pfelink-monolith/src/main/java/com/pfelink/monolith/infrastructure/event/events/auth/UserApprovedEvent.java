package com.pfelink.monolith.infrastructure.event.events.auth;

public record UserApprovedEvent(String email, String fullName) {
    public static UserApprovedEvent of(String email, String fullName) {
        return new UserApprovedEvent(email, fullName);
    }
}
