package com.pfelink.monolith.application.academic.query.project.get_by_student;

import com.pfelink.monolith.domain.academic.entity.project.Project;
import com.pfelink.monolith.domain.academic.entity.season.Season;
import com.pfelink.monolith.domain.academic.repository.IProjectRepository;
import com.pfelink.monolith.domain.academic.repository.ISeasonRepository;
import com.pfelink.monolith.shared.cqrs.IQueryHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetStudentProjectQueryHandler implements IQueryHandler<GetStudentProjectQuery, Result<Project>> {

    private final IProjectRepository projectRepository;
    private final ISeasonRepository seasonRepository;

    @Override
    public Result<Project> handle(GetStudentProjectQuery query) {
        Season season = seasonRepository.findActiveSeason().orElse(null);
        if (season == null) {
            return Result.failure(Error.failure("Season.NoActive", "No active academic season found"));
        }

        return projectRepository.findByOwnerUserIdAndSeasonId(query.studentUserId(), season.getId())
            .map(Result::success)
            .orElseGet(() -> Result.failure(Error.failure("Project.NotFound", "No project found for this student in the active season")));
    }
}
