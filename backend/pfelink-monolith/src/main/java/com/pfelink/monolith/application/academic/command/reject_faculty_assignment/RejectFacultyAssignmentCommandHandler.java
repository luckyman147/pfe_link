package com.pfelink.monolith.application.academic.command.reject_faculty_assignment;

import com.pfelink.monolith.domain.academic.entity.assignment.FacultyAssignment;
import com.pfelink.monolith.domain.academic.entity.assignment.FacultyAssignmentId;
import com.pfelink.monolith.domain.academic.entity.season.Season;
import com.pfelink.monolith.domain.academic.enums.AssignmentStatus;
import com.pfelink.monolith.domain.academic.repository.IFacultyAssignmentRepository;
import com.pfelink.monolith.domain.academic.repository.ISeasonRepository;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RejectFacultyAssignmentCommandHandler
        implements ICommandHandler<RejectFacultyAssignmentCommand, Result<Void>> {

    private final IFacultyAssignmentRepository facultyAssignmentRepository;
    private final ISeasonRepository seasonRepository;

    @Override
    @Transactional
    public Result<Void> handle(RejectFacultyAssignmentCommand cmd) {
        Season season = seasonRepository.findActiveSeason()
                .orElse(null);
        if (season == null) {
            return Result.failure(Error.failure("Season.ActiveNotFound", "No active season found"));
        }

        FacultyAssignmentId id = new FacultyAssignmentId(cmd.advisorId(), cmd.facultyId(), season.getId());
        FacultyAssignment assignment = facultyAssignmentRepository.findById(id).orElse(null);

        if (assignment == null) {
            return Result.failure(Error.failure("Assignment.NotFound", "Faculty assignment not found"));
        }

        assignment.setStatus(AssignmentStatus.REJECTED);
        facultyAssignmentRepository.save(assignment);

        return Result.success(null);
    }
}
