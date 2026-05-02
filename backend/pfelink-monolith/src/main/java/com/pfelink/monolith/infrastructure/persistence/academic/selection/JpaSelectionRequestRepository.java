package com.pfelink.monolith.infrastructure.persistence.academic.selection;

import com.pfelink.monolith.domain.academic.entity.selection.SelectionRequest;
import com.pfelink.monolith.domain.academic.repository.ISelectionRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaSelectionRequestRepository implements ISelectionRequestRepository {

    private final SpringDataSelectionRequestRepository springRepo;

    @Override public SelectionRequest save(SelectionRequest sr) { return springRepo.save(sr); }
    @Override public Optional<SelectionRequest> findById(UUID id) { return springRepo.findById(id); }
    @Override public Optional<SelectionRequest> findByProjectId(UUID projectId) { return springRepo.findByProjectId(projectId); }
    @Override public Optional<SelectionRequest> findByStudentUserId(UUID uid) { return springRepo.findByStudentUserId(uid); }
    @Override public java.util.List<SelectionRequest> findByAdvisorUserIdAndFacultyIdAndStatus(UUID userId, UUID facultyId, com.pfelink.monolith.domain.academic.enums.SelectionStatus status) {
        return springRepo.findByAdvisorUserIdAndFacultyIdAndStatus(userId, facultyId, status);
    }
    @Override public boolean existsByProjectIdAndStatus(UUID projectId, com.pfelink.monolith.domain.academic.enums.SelectionStatus status) {
        return springRepo.existsByProjectIdAndStatus(projectId, status);
    }
}
