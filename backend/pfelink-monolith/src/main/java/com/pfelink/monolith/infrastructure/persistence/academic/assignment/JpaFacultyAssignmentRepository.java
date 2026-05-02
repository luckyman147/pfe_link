package com.pfelink.monolith.infrastructure.persistence.academic.assignment;

import com.pfelink.monolith.domain.academic.entity.assignment.FacultyAssignment;
import com.pfelink.monolith.domain.academic.entity.assignment.FacultyAssignmentId;
import com.pfelink.monolith.domain.academic.repository.IFacultyAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaFacultyAssignmentRepository implements IFacultyAssignmentRepository {

    private final SpringDataFacultyAssignmentRepo springRepo;

    @Override public FacultyAssignment save(FacultyAssignment fa) { return springRepo.save(fa); }
    @Override public Optional<FacultyAssignment> findById(FacultyAssignmentId id) { return springRepo.findById(id); }
    @Override public int incrementStudentCount(FacultyAssignmentId id) { return springRepo.incrementStudentCount(id.getAdvisorId(), id.getFacultyId(), id.getSeasonId()); }
}
