package com.pfelink.monolith.application.academic.command.selection.submit;

import com.pfelink.monolith.domain.academic.entity.assignment.FacultyAssignment;
import com.pfelink.monolith.domain.academic.entity.assignment.FacultyAssignmentId;
import com.pfelink.monolith.domain.academic.entity.profile.AdvisorProfile;
import com.pfelink.monolith.domain.academic.entity.project.Project;
import com.pfelink.monolith.domain.academic.entity.selection.SelectionRequest;
import com.pfelink.monolith.domain.academic.enums.SelectionStatus;
import com.pfelink.monolith.domain.academic.repository.IAdvisorProfileRepository;
import com.pfelink.monolith.domain.academic.repository.IFacultyAssignmentRepository;
import com.pfelink.monolith.domain.academic.repository.IProjectRepository;
import com.pfelink.monolith.domain.academic.repository.ISelectionRequestRepository;
import com.pfelink.monolith.infrastructure.event.events.selection.SelectionRequestSubmittedEvent;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubmitSelectionRequestCommandHandler implements ICommandHandler<SubmitSelectionRequestCommand, Result<UUID>> {

    private final ISelectionRequestRepository selectionRequestRepository;
    private final IProjectRepository projectRepository;
    private final IAdvisorProfileRepository advisorProfileRepository;
    private final IFacultyAssignmentRepository facultyAssignmentRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Result<UUID> handle(SubmitSelectionRequestCommand cmd) {
        Project project = projectRepository.findById(cmd.projectId()).orElse(null);
        if (project == null) {
            return Result.failure(Error.failure("Project.NotFound", "Project not found"));
        }

        AdvisorProfile advisor = advisorProfileRepository.findById(cmd.advisorProfileId()).orElse(null);
        if (advisor == null) {
            return Result.failure(Error.failure("Advisor.NotFound", "Advisor profile not found"));
        }

        // Check for existing pending request
        if (selectionRequestRepository.existsByProjectIdAndStatus(project.getId(), SelectionStatus.PENDING)) {
            return Result.failure(Error.failure("Selection.AlreadyExists", "A selection request is already pending for this project."));
        }

        FacultyAssignmentId assignmentId = new FacultyAssignmentId(advisor.getId(), project.getFaculty().getId(), project.getSeason().getId());
        FacultyAssignment assignment = facultyAssignmentRepository.findById(assignmentId).orElse(null);

        if (assignment == null) {
            return Result.failure(Error.failure("Assignment.NotFound", "Advisor is not assigned to this faculty for this season"));
        }

        if (assignment.getCurrentStudentCount() + project.getMembers().size() > assignment.getMaxCapacity()) {
            return Result.failure(Error.failure("Advisor.Full", "Advisor has reached maximum student capacity for your team size"));
        }

        SelectionRequest request = new SelectionRequest();
        request.setProject(project);
        request.setAdvisor(advisor);
        request.setFaculty(project.getFaculty());
        request.setSeason(project.getSeason());
        request.setStatus(SelectionStatus.PENDING);
        request.setMessage(cmd.message());
        request.setCreatedAt(LocalDateTime.now());

        SelectionRequest saved = selectionRequestRepository.save(request);
        
        project.setStatus(com.pfelink.monolith.domain.academic.enums.ProjectStatus.PENDING_ADVISOR);
        projectRepository.save(project);

        eventPublisher.publishEvent(new SelectionRequestSubmittedEvent(LocalDateTime.now(), saved));

        return Result.success(saved.getId());
    }
}
