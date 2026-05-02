package com.pfelink.monolith.api.academic.project;

import com.pfelink.monolith.api.academic.project.dto.selection.SubmitSelectionRequest;
import com.pfelink.monolith.application.academic.command.selection.approve.ApproveSelectionRequestCommand;
import com.pfelink.monolith.application.academic.command.selection.reject.RejectSelectionRequestCommand;
import com.pfelink.monolith.application.academic.command.selection.submit.SubmitSelectionRequestCommand;
import com.pfelink.monolith.shared.cqrs.Dispatcher;
import com.pfelink.monolith.infrastructure.api.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/selection-requests")
@RequiredArgsConstructor
@Tag(name = "Advisor Selection", description = "Endpoints for students to select advisors and advisors to respond")
public class SelectionRequestController {

    private final Dispatcher dispatcher;

    @PostMapping
    @Operation(summary = "Submit a selection request (Student team to Advisor)")
    public ResponseEntity<?> submitSelection(@RequestBody SubmitSelectionRequest request) {
        return ResponseUtil.toResponse(dispatcher.send(new SubmitSelectionRequestCommand(
            request.projectId(),
            request.advisorProfileId(),
            request.message()
        )));
    }

    @GetMapping("/student/{userId}")
    @Operation(summary = "Get current selection for a student")
    public ResponseEntity<?> getStudentSelection(@PathVariable UUID userId) {
        return ResponseUtil.toResponse(dispatcher.query(new com.pfelink.monolith.application.academic.query.selection.get_by_student.GetStudentSelectionQuery(userId)));
    }

    @GetMapping("/advisor/{advisorUserId}/faculty/{facultyId}")
    @Operation(summary = "Get all students for an advisor in a faculty")
    public ResponseEntity<?> getAdvisorStudents(@PathVariable UUID advisorUserId, @PathVariable UUID facultyId) {
        return ResponseUtil.toResponse(dispatcher.query(new com.pfelink.monolith.application.academic.query.selection.get_advisor_students.GetAdvisorStudentsQuery(advisorUserId, facultyId)));
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve a selection request (Advisor)")
    public ResponseEntity<?> approveSelection(@PathVariable UUID id) {
        return ResponseUtil.toResponse(dispatcher.send(new ApproveSelectionRequestCommand(id)));
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject a selection request (Advisor)")
    public ResponseEntity<?> rejectSelection(@PathVariable UUID id) {
        return ResponseUtil.toResponse(dispatcher.send(new RejectSelectionRequestCommand(id)));
    }
}
