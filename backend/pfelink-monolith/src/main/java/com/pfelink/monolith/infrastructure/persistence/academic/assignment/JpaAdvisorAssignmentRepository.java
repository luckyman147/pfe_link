package com.pfelink.monolith.infrastructure.persistence.academic.assignment;

import com.pfelink.monolith.domain.academic.entity.assignment.AdvisorAssignment;
import com.pfelink.monolith.domain.academic.enums.VerificationStatus;
import com.pfelink.monolith.domain.academic.repository.IAdvisorAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaAdvisorAssignmentRepository implements IAdvisorAssignmentRepository {

    private final SpringDataAdvisorAssignmentRepository springRepo;

    @Override public AdvisorAssignment save(AdvisorAssignment a) { return springRepo.save(a); }
    @Override public Optional<AdvisorAssignment> findById(UUID id) { return springRepo.findById(id); }
    @Override public int incrementStudentCount(UUID id) { return springRepo.incrementStudentCount(id); }
    @Override public List<AdvisorAssignment> findAllPending() { return springRepo.findByStatus(VerificationStatus.PENDING); }
    @Override public List<AdvisorAssignment> findByFacultyIdAndStatus(UUID facultyId, VerificationStatus status) {
        return springRepo.findByFacultyIdAndStatus(facultyId, status);
    }
}
