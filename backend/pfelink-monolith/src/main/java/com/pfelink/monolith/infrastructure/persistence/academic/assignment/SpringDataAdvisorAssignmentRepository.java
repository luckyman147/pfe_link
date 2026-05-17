package com.pfelink.monolith.infrastructure.persistence.academic.assignment;

import com.pfelink.monolith.domain.academic.entity.assignment.AdvisorAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SpringDataAdvisorAssignmentRepository extends JpaRepository<AdvisorAssignment, UUID> {
    List<AdvisorAssignment> findByStatus(com.pfelink.monolith.domain.academic.enums.student.VerificationStatus status);
    List<AdvisorAssignment> findByFacultyIdAndStatus(UUID facultyId, com.pfelink.monolith.domain.academic.enums.student.VerificationStatus status);

    @Modifying
    @Query("UPDATE AdvisorAssignment a SET a.currentStudentCount = a.currentStudentCount + 1 WHERE a.id = :id")
    int incrementStudentCount(UUID id);
}
