package com.pfelink.monolith.application.academic.command.approve_faculty_assignment;

import com.pfelink.monolith.domain.academic.entity.assignment.FacultyAssignment;
import com.pfelink.monolith.domain.academic.entity.assignment.FacultyAssignmentId;
import com.pfelink.monolith.domain.academic.entity.season.Season;
import com.pfelink.monolith.domain.academic.enums.faculty.AssignmentStatus;
import com.pfelink.monolith.domain.academic.repository.IFacultyAssignmentRepository;
import com.pfelink.monolith.domain.academic.repository.ISeasonRepository;
import com.pfelink.monolith.infrastructure.event.events.assignment.FacultyAssignmentApprovedEvent;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApproveFacultyAssignmentCommandHandler
        implements ICommandHandler<ApproveFacultyAssignmentCommand, Result<UUID>> {

    private final IFacultyAssignmentRepository facultyAssignmentRepository;
    private final ISeasonRepository seasonRepository;
    private final org.springframework.context.ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Result<UUID> handle(ApproveFacultyAssignmentCommand cmd) {
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

        assignment.setStatus(AssignmentStatus.APPROVED);
        facultyAssignmentRepository.save(assignment);

        eventPublisher.publishEvent(FacultyAssignmentApprovedEvent.of(
                java.time.LocalDateTime.now(),
                assignment.getAdvisor().getUserId(),
                assignment.getAdvisor().getEmail(),
                assignment.getAdvisor().getFullName(),
                assignment.getFaculty().getName(),
                season.getName()
        ));

        return Result.success(assignment.getFaculty().getId());
    }
}
