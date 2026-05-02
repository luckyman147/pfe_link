package com.pfelink.monolith.domain.academic.repository;

import com.pfelink.monolith.domain.academic.entity.assignment.FacultyAssignment;
import com.pfelink.monolith.domain.academic.entity.assignment.FacultyAssignmentId;

import java.util.Optional;

public interface IFacultyAssignmentRepository {
    FacultyAssignment save(FacultyAssignment fa);
    Optional<FacultyAssignment> findById(FacultyAssignmentId id);
    int incrementStudentCount(FacultyAssignmentId id);
}
