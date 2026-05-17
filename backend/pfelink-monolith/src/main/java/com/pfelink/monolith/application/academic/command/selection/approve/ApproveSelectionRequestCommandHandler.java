package com.pfelink.monolith.application.academic.command.selection.approve;

import com.pfelink.monolith.domain.academic.entity.assignment.FacultyAssignment;
import com.pfelink.monolith.domain.academic.entity.assignment.FacultyAssignmentId;
import com.pfelink.monolith.domain.academic.entity.project.Project;
import com.pfelink.monolith.domain.academic.entity.selection.SelectionRequest;
import com.pfelink.monolith.domain.academic.enums.project.ProjectStatus;
import com.pfelink.monolith.domain.academic.enums.project.SelectionStatus;
import com.pfelink.monolith.domain.academic.repository.IFacultyAssignmentRepository;
import com.pfelink.monolith.domain.academic.repository.IProjectRepository;
import com.pfelink.monolith.domain.academic.repository.ISelectionRequestRepository;
import com.pfelink.monolith.infrastructure.event.events.selection.SelectionRequestRespondedEvent;
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
public class ApproveSelectionRequestCommandHandler implements ICommandHandler<ApproveSelectionRequestCommand, Result<Void>> {

    private final ISelectionRequestRepository selectionRequestRepository;
    private final IFacultyAssignmentRepository facultyAssignmentRepository;
    private final IProjectRepository projectRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Result<Void> handle(ApproveSelectionRequestCommand cmd) {
        SelectionRequest request = selectionRequestRepository.findById(cmd.requestId()).orElse(null);
        if (request == null) {
            return Result.failure(Error.failure("Selection.NotFound", "Selection request not found"));
        }

        if (request.getStatus() != SelectionStatus.PENDING) {
            return Result.failure(Error.failure("Selection.InvalidStatus", "Request is already " + request.getStatus()));
        }

        Project project = request.getProject();
        FacultyAssignmentId assignmentId = new FacultyAssignmentId(
            request.getAdvisor().getId(), 
            request.getFaculty().getId(), 
            request.getSeason().getId()
        );
        
        FacultyAssignment assignment = facultyAssignmentRepository.findById(assignmentId).orElse(null);
        if (assignment == null) {
            return Result.failure(Error.failure("Assignment.NotFound", "Advisor assignment not found"));
        }

        int groupSize = project.getMembers().size();
        if (assignment.getCurrentStudentCount() + groupSize > assignment.getMaxCapacity()) {
            return Result.failure(Error.failure("Advisor.Full", "Maximum capacity reached for this team size"));
        }

        request.setStatus(SelectionStatus.APPROVED);
        request.setRespondedAt(LocalDateTime.now());
        selectionRequestRepository.save(request);

        project.setAdvisor(request.getAdvisor());
        project.setStatus(ProjectStatus.APPROVED);
        projectRepository.save(project);

        assignment.setCurrentStudentCount(assignment.getCurrentStudentCount() + groupSize);
        facultyAssignmentRepository.save(assignment);

        eventPublisher.publishEvent(new SelectionRequestRespondedEvent(LocalDateTime.now(), request));

        return Result.success(null);
    }
}
