package com.pfelink.monolith.infrastructure.persistence.academic.assignment;

import com.pfelink.monolith.domain.academic.entity.assignment.FacultyAssignment;
import com.pfelink.monolith.domain.academic.entity.assignment.FacultyAssignmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface SpringDataFacultyAssignmentRepo extends JpaRepository<FacultyAssignment, FacultyAssignmentId> {

    @Modifying
    @Query("UPDATE FacultyAssignment fa SET fa.currentStudentCount = fa.currentStudentCount + 1 " +
           "WHERE fa.id.advisorId = :advisorId AND fa.id.facultyId = :facultyId AND fa.id.seasonId = :seasonId")
    int incrementStudentCount(UUID advisorId, UUID facultyId, UUID seasonId);
}
