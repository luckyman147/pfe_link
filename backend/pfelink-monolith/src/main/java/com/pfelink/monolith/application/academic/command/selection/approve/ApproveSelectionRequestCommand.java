package com.pfelink.monolith.application.academic.command.selection.approve;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;
import java.util.UUID;

public record ApproveSelectionRequestCommand(
    UUID requestId
) implements ICommand<Result<Void>> {}
