package com.pfelink.monolith.application.storage.command.upload_draft;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public record UploadDraftCommand(
    MultipartFile file
) implements ICommand<Result<Map<String, String>>> {}
