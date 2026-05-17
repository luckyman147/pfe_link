package com.pfelink.monolith.infrastructure.persistence.academic.selection;

import com.pfelink.monolith.domain.academic.entity.selection.SelectionRequest;
import com.pfelink.monolith.domain.academic.enums.project.SelectionStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataSelectionRequestRepository extends JpaRepository<SelectionRequest, UUID> {
    Optional<SelectionRequest> findByProjectId(UUID projectId);

    @org.springframework.data.jpa.repository.Query("SELECT sr FROM SelectionRequest sr JOIN sr.project p JOIN p.members m WHERE m.userId = :studentUserId")
    Optional<SelectionRequest> findByStudentUserId(UUID studentUserId);

    boolean existsByProjectIdAndStatus(UUID projectId, SelectionStatus status);

    @org.springframework.data.jpa.repository.Query("SELECT sr FROM SelectionRequest sr JOIN sr.advisor a WHERE a.userId = :userId AND sr.faculty.id = :facultyId AND sr.status = :status")
    java.util.List<SelectionRequest> findByAdvisorUserIdAndFacultyIdAndStatus(UUID userId, UUID facultyId, SelectionStatus status);
}
