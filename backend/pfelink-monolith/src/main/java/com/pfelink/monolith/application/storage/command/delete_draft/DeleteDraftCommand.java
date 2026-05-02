package com.pfelink.monolith.application.storage.command.delete_draft;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;

public record DeleteDraftCommand(
    String publicId
) implements ICommand<Result<Void>> {}
