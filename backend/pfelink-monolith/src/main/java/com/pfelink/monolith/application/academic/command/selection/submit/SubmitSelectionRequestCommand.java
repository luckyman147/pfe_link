package com.pfelink.monolith.application.academic.command.selection.submit;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;
import java.util.UUID;

public record SubmitSelectionRequestCommand(
    UUID projectId,
    UUID advisorProfileId,
    String message
) implements ICommand<Result<UUID>> {}
