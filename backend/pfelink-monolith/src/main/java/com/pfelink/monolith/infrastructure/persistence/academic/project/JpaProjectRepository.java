package com.pfelink.monolith.infrastructure.persistence.academic.project;

import com.pfelink.monolith.domain.academic.entity.project.Project;
import com.pfelink.monolith.domain.academic.repository.IProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaProjectRepository implements IProjectRepository {
    private final SpringDataProjectRepository springRepo;

    @Override public Project save(Project project) { return springRepo.save(project); }
    @Override public Optional<Project> findById(UUID id) { return springRepo.findById(id); }
    @Override public Optional<Project> findByOwnerUserIdAndSeasonId(UUID userId, UUID seasonId) { 
        return springRepo.findByOwnerUserIdAndSeasonId(userId, seasonId); 
    }
    @Override public List<Project> findByFacultyId(UUID facultyId) { return springRepo.findByFacultyId(facultyId); }
}
