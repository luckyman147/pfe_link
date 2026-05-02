package com.pfelink.monolith.infrastructure.persistence.academic.project;

import com.pfelink.monolith.domain.academic.entity.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataProjectRepository extends JpaRepository<Project, UUID> {
    @Query("SELECT p FROM Project p WHERE p.owner.userId = :userId AND p.season.id = :seasonId")
    Optional<Project> findByOwnerUserIdAndSeasonId(UUID userId, UUID seasonId);
    
    java.util.List<Project> findByFacultyId(UUID facultyId);
}
