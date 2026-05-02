package com.pfelink.monolith.application.academic.command.selection.reject;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;
import java.util.UUID;

public record RejectSelectionRequestCommand(
    UUID requestId
) implements ICommand<Result<Void>> {}
