package com.pfelink.monolith.domain.academic.repository;

import com.pfelink.monolith.domain.academic.entity.project.ProjectInvitation;
import com.pfelink.monolith.domain.academic.enums.ProjectInvitationStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProjectInvitationRepository {
    ProjectInvitation save(ProjectInvitation invitation);
    Optional<ProjectInvitation> findById(UUID id);
    List<ProjectInvitation> findByInviteeUserId(UUID userId);
    List<ProjectInvitation> findByProjectId(UUID projectId);
    List<ProjectInvitation> findByInviteeUserIdAndStatus(UUID userId, ProjectInvitationStatus status);
}
