package com.pfelink.monolith.application.storage.command.delete_draft;

import com.pfelink.monolith.infrastructure.storage.IStorageService;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteDraftCommandHandler
        implements ICommandHandler<DeleteDraftCommand, Result<Void>> {

    private final IStorageService storageService;

    @Override
    public Result<Void> handle(DeleteDraftCommand cmd) {
        try {
            storageService.delete(cmd.publicId());
            return Result.success(null);
        } catch (Exception e) {
            return Result.failure(
                com.pfelink.monolith.shared.result.Error.failure("Delete.Failed", e.getMessage())
            );
        }
    }
}
