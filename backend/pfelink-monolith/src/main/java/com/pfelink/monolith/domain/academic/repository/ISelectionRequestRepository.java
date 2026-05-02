package com.pfelink.monolith.domain.academic.repository;

import com.pfelink.monolith.domain.academic.entity.selection.SelectionRequest;

import java.util.Optional;
import java.util.UUID;

public interface ISelectionRequestRepository {
    SelectionRequest save(SelectionRequest selectionRequest);
    Optional<SelectionRequest> findById(UUID id);
    Optional<SelectionRequest> findByProjectId(UUID projectId);
    Optional<SelectionRequest> findByStudentUserId(UUID studentUserId);
    java.util.List<SelectionRequest> findByAdvisorUserIdAndFacultyIdAndStatus(UUID userId, UUID facultyId, com.pfelink.monolith.domain.academic.enums.SelectionStatus status);
    boolean existsByProjectIdAndStatus(UUID projectId, com.pfelink.monolith.domain.academic.enums.SelectionStatus status);
}
