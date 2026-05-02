package com.pfelink.monolith.api.storage;

import com.pfelink.monolith.application.storage.command.delete_draft.DeleteDraftCommand;
import com.pfelink.monolith.application.storage.command.upload_draft.UploadDraftCommand;
import com.pfelink.monolith.infrastructure.api.ResponseUtil;
import com.pfelink.monolith.shared.cqrs.Dispatcher;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final Dispatcher dispatcher;

    @PostMapping("/draft")
    public ResponseEntity<?> uploadDraft(@RequestParam("file") MultipartFile file) {
        return ResponseUtil.toResponse(dispatcher.send(new UploadDraftCommand(file)));
    }

    @DeleteMapping("/draft/{publicId}")
    public ResponseEntity<?> deleteDraft(@PathVariable String publicId) {
        return ResponseUtil.toResponse(dispatcher.send(new DeleteDraftCommand(publicId)));
    }
}
