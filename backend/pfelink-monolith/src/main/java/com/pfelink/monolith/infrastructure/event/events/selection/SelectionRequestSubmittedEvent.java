package com.pfelink.monolith.infrastructure.event.events.selection;

import com.pfelink.monolith.domain.academic.entity.selection.SelectionRequest;
import java.time.LocalDateTime;

public record SelectionRequestSubmittedEvent(
    LocalDateTime timestamp,
    SelectionRequest request
) {
    public static SelectionRequestSubmittedEvent of(LocalDateTime timestamp, SelectionRequest request) {
        return new SelectionRequestSubmittedEvent(timestamp, request);
    }
}
