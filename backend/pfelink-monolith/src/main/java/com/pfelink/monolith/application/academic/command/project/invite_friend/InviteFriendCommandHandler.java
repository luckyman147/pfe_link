package com.pfelink.monolith.application.academic.command.project.invite_friend;

import com.pfelink.monolith.domain.academic.entity.profile.StudentProfile;
import com.pfelink.monolith.domain.academic.entity.project.Project;
import com.pfelink.monolith.domain.academic.entity.project.ProjectInvitation;
import com.pfelink.monolith.domain.academic.repository.IProjectInvitationRepository;
import com.pfelink.monolith.domain.academic.repository.IProjectRepository;
import com.pfelink.monolith.domain.academic.repository.IStudentProfileRepository;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteFriendCommandHandler implements ICommandHandler<InviteFriendCommand, Result<UUID>> {

    private final IProjectRepository projectRepository;
    private final IStudentProfileRepository studentProfileRepository;
    private final IProjectInvitationRepository invitationRepository;

    @Override
    @Transactional
    public Result<UUID> handle(InviteFriendCommand cmd) {
        Project project = projectRepository.findById(cmd.projectId()).orElse(null);
        if (project == null) {
            return Result.failure(Error.failure("Project.NotFound", "Project not found"));
        }

        // Ownership check
        if (!project.getOwner().getUserId().equals(cmd.ownerId())) {
            return Result.failure(Error.failure("Project.Unauthorized", "You are not the owner of this project"));
        }

        StudentProfile invitee = studentProfileRepository.findById(cmd.inviteeStudentProfileId()).orElse(null);
        if (invitee == null) {
            return Result.failure(Error.failure("Student.NotFound", "Invitee student profile not found"));
        }

        if (invitee.getProject() != null) {
            return Result.failure(Error.failure("Student.HasProject", "Student already belongs to a project"));
        }

        ProjectInvitation invitation = new ProjectInvitation();
        invitation.setProject(project);
        invitation.setInvitee(invitee);
        
        ProjectInvitation saved = invitationRepository.save(invitation);
        
        return Result.success(saved.getId());
    }
}
