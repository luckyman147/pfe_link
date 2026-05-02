package com.pfelink.monolith.infrastructure.dispatcher;

public class HandlerNotFoundException extends RuntimeException {

    public HandlerNotFoundException(Class<?> type) {
        super("No handler registered for: " + type.getName());
    }
}
