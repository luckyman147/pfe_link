package com.pfelink.monolith.api.storage;

import com.pfelink.monolith.application.storage.command.delete_draft.DeleteDraftCommand;
import com.pfelink.monolith.application.storage.command.upload_draft.UploadDraftCommand;
import com.pfelink.monolith.infrastructure.api.ResponseUtil;
import com.pfelink.monolith.infrastructure.storage.FileValidationUtil;
import com.pfelink.monolith.shared.cqrs.Dispatcher;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/uploads")
@RequiredArgsConstructor
@Slf4j
public class UploadController {

    private final Dispatcher dispatcher;

    @PostMapping("/draft")
    public ResponseEntity<?> uploadDraft(@RequestParam("file") MultipartFile file) {
        try {
            // Validate file before processing
            FileValidationUtil.validateFile(file);

            String sanitizedFilename = FileValidationUtil.sanitizeFilename(file.getOriginalFilename());
            log.info("File upload attempt: {}", sanitizedFilename);

            return ResponseUtil.toResponse(dispatcher.send(new UploadDraftCommand(file)));
        } catch (FileValidationUtil.FileValidationException e) {
            log.warn("File validation failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(Result.failure(Error.failure("Upload.ValidationFailed", e.getMessage())));
        }
    }

    @DeleteMapping("/draft/{publicId}")
    public ResponseEntity<?> deleteDraft(@PathVariable String publicId) {
        try {
            // Get current user ID for authorization check
            String userId = SecurityContextHolder.getContext()
                .getAuthentication().getName();

            if (publicId == null || publicId.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Result.failure(Error.failure("Upload.InvalidId", "File ID is required")));
            }

            // Verify user owns this file in the DeleteDraftCommand handler
            return ResponseUtil.toResponse(dispatcher.send(new DeleteDraftCommand(publicId)));
        } catch (Exception e) {
            log.error("Error deleting draft: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.failure(Error.failure("Upload.DeleteFailed", "Failed to delete file")));
        }
    }
}
