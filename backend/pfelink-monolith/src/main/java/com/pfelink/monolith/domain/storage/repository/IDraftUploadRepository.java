package com.pfelink.monolith.domain.storage.repository;

import com.pfelink.monolith.domain.storage.entity.DraftUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IDraftUploadRepository extends JpaRepository<DraftUpload, String> {
    List<DraftUpload> findAllByCommittedFalseAndCreatedAtBefore(LocalDateTime threshold);
}
