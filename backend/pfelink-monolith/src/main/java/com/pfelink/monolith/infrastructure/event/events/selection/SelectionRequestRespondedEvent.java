package com.pfelink.monolith.infrastructure.event.events.selection;

import com.pfelink.monolith.domain.academic.entity.selection.SelectionRequest;
import java.time.LocalDateTime;

public record SelectionRequestRespondedEvent(
    LocalDateTime timestamp,
    SelectionRequest request
) {}
