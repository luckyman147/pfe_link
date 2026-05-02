package com.pfelink.monolith.application.storage.command.upload_draft;

import com.pfelink.monolith.domain.storage.entity.DraftUpload;
import com.pfelink.monolith.domain.storage.repository.IDraftUploadRepository;
import com.pfelink.monolith.infrastructure.storage.IStorageService;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadDraftCommandHandler
        implements ICommandHandler<UploadDraftCommand, Result<Map<String, String>>> {

    private final IStorageService storageService;
    private final IDraftUploadRepository draftRepository;
    private final com.pfelink.monolith.infrastructure.storage.util.FileSignatureValidator signatureValidator;

    @Override
    public Result<Map<String, String>> handle(UploadDraftCommand cmd) {
        try {
            // 1. Validate Magic Bytes
            if (!signatureValidator.isValid(cmd.file().getInputStream(), cmd.file().getContentType())) {
                return Result.failure(
                    com.pfelink.monolith.shared.result.Error.failure("Upload.InvalidFileSignature", "File content does not match its extension.")
                );
            }

            // 2. Perform Upload
            Map<String, String> uploaded = storageService.upload(cmd.file());

            DraftUpload draft = new DraftUpload();
            draft.setId(UUID.randomUUID().toString());
            draft.setUrl(uploaded.get("url"));
            draft.setPublicId(uploaded.get("public_id"));
            draftRepository.save(draft);

            return Result.success(Map.of(
                "draftId", draft.getId(),
                "url", draft.getUrl()
            ));
        } catch (Exception e) {
            return Result.failure(
                com.pfelink.monolith.shared.result.Error.failure("Upload.Failed", e.getMessage())
            );
        }
    }
}
