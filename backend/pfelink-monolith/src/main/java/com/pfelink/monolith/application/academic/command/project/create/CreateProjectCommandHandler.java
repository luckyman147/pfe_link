package com.pfelink.monolith.application.academic.command.project.create;

import com.pfelink.monolith.domain.academic.entity.profile.StudentProfile;
import com.pfelink.monolith.domain.academic.entity.project.Project;
import com.pfelink.monolith.domain.academic.entity.season.Season;
import com.pfelink.monolith.domain.academic.enums.ProjectStatus;
import com.pfelink.monolith.domain.academic.repository.IProjectRepository;
import com.pfelink.monolith.domain.academic.repository.ISeasonRepository;
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
public class CreateProjectCommandHandler implements ICommandHandler<CreateProjectCommand, Result<UUID>> {

    private final IProjectRepository projectRepository;
    private final IStudentProfileRepository studentProfileRepository;
    private final ISeasonRepository seasonRepository;

    @Override
    @Transactional
    public Result<UUID> handle(CreateProjectCommand cmd) {
        Season season = seasonRepository.findActiveSeason().orElse(null);
        if (season == null) {
            return Result.failure(Error.failure("Season.NoActive", "No active academic season found"));
        }

        StudentProfile owner = studentProfileRepository.findByUserId(cmd.studentUserId()).orElse(null);
        if (owner == null) {
            return Result.failure(Error.failure("Student.NotFound", "Student profile not found"));
        }

        // Check if student already has a project in this season
        if (projectRepository.findByOwnerUserIdAndSeasonId(cmd.studentUserId(), season.getId()).isPresent()) {
            return Result.failure(Error.failure("Project.AlreadyExists", "You already have a project for this season"));
        }

        Project project = new Project();
        project.setTitle(cmd.title());
        project.setDescription(cmd.description());
        project.setOwner(owner);
        project.setFaculty(owner.getFaculty());
        project.setSeason(season);
        project.setStatus(ProjectStatus.DRAFT);
        
        Project saved = projectRepository.save(project);
        
        // Link owner to the project
        owner.setProject(saved);
        studentProfileRepository.save(owner);

        return Result.success(saved.getId());
    }
}
