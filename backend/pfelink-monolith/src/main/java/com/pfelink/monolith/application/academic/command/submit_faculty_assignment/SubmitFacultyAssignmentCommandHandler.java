package com.pfelink.monolith.application.academic.command.submit_faculty_assignment;

import com.pfelink.monolith.domain.academic.entity.assignment.FacultyAssignment;
import com.pfelink.monolith.domain.academic.entity.assignment.FacultyAssignmentId;
import com.pfelink.monolith.domain.academic.entity.profile.AdvisorProfile;
import com.pfelink.monolith.domain.academic.entity.season.Season;
import com.pfelink.monolith.domain.academic.enums.AssignmentStatus;
import com.pfelink.monolith.domain.academic.repository.IAdvisorProfileRepository;
import com.pfelink.monolith.domain.academic.repository.IFacultyAssignmentRepository;
import com.pfelink.monolith.domain.academic.repository.IFacultyRepository;
import com.pfelink.monolith.domain.academic.repository.ISeasonRepository;
import com.pfelink.monolith.infrastructure.event.events.assignment.FacultyAssignmentSubmittedEvent;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubmitFacultyAssignmentCommandHandler
        implements ICommandHandler<SubmitFacultyAssignmentCommand, Result<UUID>> {

    private final IAdvisorProfileRepository advisorProfileRepository;
    private final IFacultyRepository facultyRepository;
    private final ISeasonRepository seasonRepository;
    private final IFacultyAssignmentRepository facultyAssignmentRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Result<UUID> handle(SubmitFacultyAssignmentCommand cmd) {
        AdvisorProfile advisor = advisorProfileRepository.findById(cmd.advisorId())
                .orElse(null);
        if (advisor == null) {
            return Result.failure(Error.failure("Advisor.NotFound", "Advisor profile not found"));
        }

        Season activeSeason = seasonRepository.findActiveSeason()
                .orElse(null);
        if (activeSeason == null) {
            return Result.failure(Error.failure("Season.NoActive", "No active academic season found"));
        }

        var faculty = facultyRepository.findById(cmd.facultyId()).orElse(null);
        if (faculty == null) {
            return Result.failure(Error.failure("Faculty.NotFound", "Faculty not found"));
        }

        FacultyAssignmentId assignmentId = new FacultyAssignmentId(advisor.getId(), faculty.getId(), activeSeason.getId());
        if (facultyAssignmentRepository.findById(assignmentId).isPresent()) {
            return Result.failure(Error.failure("Assignment.Exists", "An assignment for this faculty already exists in the active season"));
        }

        FacultyAssignment assignment = new FacultyAssignment();
        assignment.setId(assignmentId);
        assignment.setAdvisor(advisor);
        assignment.setFaculty(faculty);
        assignment.setSeason(activeSeason);
        assignment.setAdvisorRole(cmd.advisorRole());
        assignment.setMaxCapacity(cmd.maxCapacity());
        assignment.setFacultyDomainEmail(cmd.facultyDomainEmail());
        assignment.setProfessionalProofUrl(cmd.professionalProofUrl());
        assignment.setStatus(AssignmentStatus.PENDING);

        facultyAssignmentRepository.save(assignment);

        eventPublisher.publishEvent(FacultyAssignmentSubmittedEvent.of(
                advisor.getId(),
                faculty.getId(),
                advisor.getFullName(),
                advisor.getEmail(),
                advisor.getTelephone(),
                advisor.getCinNumber(),
                cmd.advisorRole(),
                cmd.maxCapacity(),
                cmd.facultyDomainEmail(),
                cmd.professionalProofUrl()
        ));

        return Result.success(faculty.getId()); // Returning facultyId as part of composite key
    }
}
