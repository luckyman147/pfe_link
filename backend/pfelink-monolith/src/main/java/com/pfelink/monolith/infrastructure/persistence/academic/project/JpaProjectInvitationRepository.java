package com.pfelink.monolith.infrastructure.persistence.academic.project;

import com.pfelink.monolith.domain.academic.entity.project.ProjectInvitation;
import com.pfelink.monolith.domain.academic.enums.ProjectInvitationStatus;
import com.pfelink.monolith.domain.academic.repository.IProjectInvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaProjectInvitationRepository implements IProjectInvitationRepository {
    private final SpringDataProjectInvitationRepository springRepo;

    @Override public ProjectInvitation save(ProjectInvitation invitation) { return springRepo.save(invitation); }
    @Override public Optional<ProjectInvitation> findById(UUID id) { return springRepo.findById(id); }
    @Override public List<ProjectInvitation> findByInviteeUserId(UUID userId) { return springRepo.findByInviteeUserId(userId); }
    @Override public List<ProjectInvitation> findByProjectId(UUID projectId) { return springRepo.findByProjectId(projectId); }
    @Override
    public List<ProjectInvitation> findByInviteeUserIdAndStatus(UUID userId, ProjectInvitationStatus status) {
        return springRepo.findAllByInviteeUserIdAndStatus(userId, status);
    }
}
