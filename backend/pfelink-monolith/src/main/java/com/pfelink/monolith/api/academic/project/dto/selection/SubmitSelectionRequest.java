package com.pfelink.monolith.api.academic.project.dto.selection;

import java.util.UUID;

public record SubmitSelectionRequest(
    UUID projectId,
    UUID advisorProfileId,
    String message
) {}
