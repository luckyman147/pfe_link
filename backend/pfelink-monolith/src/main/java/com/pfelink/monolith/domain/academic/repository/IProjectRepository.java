package com.pfelink.monolith.domain.academic.repository;

import com.pfelink.monolith.domain.academic.entity.project.Project;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProjectRepository {
    Project save(Project project);
    Optional<Project> findById(UUID id);
    Optional<Project> findByOwnerUserIdAndSeasonId(UUID userId, UUID seasonId);
    List<Project> findByFacultyId(UUID facultyId);
}
