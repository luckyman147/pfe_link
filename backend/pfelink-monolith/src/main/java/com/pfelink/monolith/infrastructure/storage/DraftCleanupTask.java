package com.pfelink.monolith.infrastructure.storage;

import com.pfelink.monolith.domain.storage.entity.DraftUpload;
import com.pfelink.monolith.domain.storage.repository.IDraftUploadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DraftCleanupTask {

    private final IDraftUploadRepository draftRepository;
    private final IStorageService storageService;

    /**
     * Delete uncommitted drafts older than 24 hours.
     * Runs every hour.
     */
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void cleanupUnusedDrafts() {
        log.info("Starting cleanup of unused draft uploads...");
        
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);
        List<DraftUpload> expiredDrafts = draftRepository.findAllByCommittedFalseAndCreatedAtBefore(threshold);

        if (expiredDrafts.isEmpty()) {
            log.info("No expired drafts found.");
            return;
        }

        for (DraftUpload draft : expiredDrafts) {
            try {
                storageService.delete(draft.getPublicId());
                draftRepository.delete(draft);
                log.info("Deleted expired draft: {}", draft.getId());
            } catch (IOException e) {
                log.error("Failed to delete from Cloudinary {}: {}", draft.getPublicId(), e.getMessage());
            } catch (Exception e) {
                log.error("Failed to delete expired draft {}: {}", draft.getId(), e.getMessage());
            }
        }

        log.info("Finished cleanup of {} unused draft uploads.", expiredDrafts.size());
    }
}
