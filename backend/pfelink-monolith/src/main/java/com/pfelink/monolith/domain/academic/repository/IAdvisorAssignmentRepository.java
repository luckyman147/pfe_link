package com.pfelink.monolith.domain.academic.repository;

import com.pfelink.monolith.domain.academic.entity.assignment.AdvisorAssignment;
import com.pfelink.monolith.domain.academic.enums.student.VerificationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAdvisorAssignmentRepository {
    AdvisorAssignment save(AdvisorAssignment advisorAssignment);
    Optional<AdvisorAssignment> findById(UUID id);
    int incrementStudentCount(UUID id);
    List<AdvisorAssignment> findAllPending();
    List<AdvisorAssignment> findByFacultyIdAndStatus(UUID facultyId, VerificationStatus status);
}
