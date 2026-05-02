package com.pfelink.monolith.infrastructure.storage.scheduler;

import com.pfelink.monolith.domain.storage.entity.DraftUpload;
import com.pfelink.monolith.domain.storage.repository.IDraftUploadRepository;
import com.pfelink.monolith.infrastructure.storage.IStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DraftUploadPurgeTask {

    private final IDraftUploadRepository draftRepository;
    private final IStorageService storageService;

    /**
     * Purges uncommitted draft uploads older than 24 hours.
     * Runs every hour at the top of the hour.
     */
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void purgeOldDrafts() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);
        List<DraftUpload> oldDrafts = draftRepository.findAllByCommittedFalseAndCreatedAtBefore(threshold);

        if (oldDrafts.isEmpty()) {
            return;
        }

        log.info("Starting purge of {} stale draft uploads...", oldDrafts.size());

        for (DraftUpload draft : oldDrafts) {
            try {
                storageService.delete(draft.getPublicId());
                draftRepository.delete(draft);
                log.info("Purged draft file: {}", draft.getPublicId());
            } catch (Exception e) {
                log.error("Failed to purge draft file {}: {}", draft.getPublicId(), e.getMessage());
            }
        }

        log.info("Draft purge completed.");
    }
}
