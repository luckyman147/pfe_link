package com.pfelink.monolith.shared.result;

public record Error(String code, String message) {

    public static final Error NONE = new Error("", "");

    public static final Error NULL_VALUE =
            new Error("Error.NullValue", "The specified result value is null.");

    public static Error failure(String code, String message) {
        return new Error(code, message);
    }

    public static Error validation(String message) {
        return new Error("Error.Validation", message);
    }

    public static Error notFound(String message) {
        return new Error("Error.NotFound", message);
    }
}

