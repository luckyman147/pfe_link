package com.pfelink.monolith.shared.result;

import java.util.Optional;

public class Result<T> {

    private final T value;
    private final boolean isSuccess;
    private final Error error;

    protected Result(T value, boolean isSuccess, Error error) {
        if (isSuccess && error != Error.NONE) {
            throw new IllegalArgumentException("Invalid error for success result");
        }
        if (!isSuccess && error == Error.NONE) {
            throw new IllegalArgumentException("Invalid error for failure result");
        }
        this.value = value;
        this.isSuccess = isSuccess;
        this.error = error;
    }

    public boolean isSuccess() { return isSuccess; }

    public boolean isFailure() { return !isSuccess; }

    public T getValue() {
        if (isFailure()) {
            throw new IllegalStateException("Cannot access value of a failure result.");
        }
        return value;
    }

    public Error getError() { return error; }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, true, Error.NONE);
    }

    public static <T> Result<T> failure(Error error) {
        return new Result<>(null, false, error);
    }

    public static <T> Result<T> from(Optional<T> optional, Error errorIfEmpty) {
        return optional.map(Result::success)
                .orElseGet(() -> Result.failure(errorIfEmpty));
    }
}
