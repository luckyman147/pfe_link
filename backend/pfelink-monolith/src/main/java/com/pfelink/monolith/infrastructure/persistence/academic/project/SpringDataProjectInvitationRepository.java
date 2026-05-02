package com.pfelink.monolith.infrastructure.persistence.academic.project;

import com.pfelink.monolith.domain.academic.entity.project.ProjectInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SpringDataProjectInvitationRepository extends JpaRepository<ProjectInvitation, UUID> {
    List<ProjectInvitation> findByInviteeUserId(UUID userId);
    List<ProjectInvitation> findByProjectId(UUID projectId);
}
