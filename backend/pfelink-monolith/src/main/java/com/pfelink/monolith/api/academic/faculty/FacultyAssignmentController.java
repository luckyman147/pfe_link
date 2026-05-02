package com.pfelink.monolith.api.academic.faculty;

import com.pfelink.monolith.application.academic.command.submit_faculty_assignment.SubmitFacultyAssignmentCommand;
import com.pfelink.monolith.shared.cqrs.Dispatcher;
import com.pfelink.monolith.infrastructure.api.ResponseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/faculty-assignments")
@RequiredArgsConstructor
@Tag(name = "Faculty Assignments", description = "Advisors submitting assignment requests")
public class FacultyAssignmentController {

    private final Dispatcher dispatcher;

    @PostMapping
    public ResponseEntity<?> submitAssignment(@Valid @RequestBody SubmitFacultyAssignmentCommand command) {
        return ResponseUtil.toResponse(dispatcher.send(command));
    }
}
