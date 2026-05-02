package com.pfelink.monolith.api.academic.faculty.action;

import com.pfelink.monolith.application.academic.command.approve_faculty.ApproveFacultyCommand;
import com.pfelink.monolith.application.academic.command.reject_faculty.RejectFacultyCommand;
import com.pfelink.monolith.application.common.HtmlResponseService;
import com.pfelink.monolith.shared.cqrs.Dispatcher;
import com.pfelink.monolith.shared.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/faculty-action")
@RequiredArgsConstructor
@Tag(name = "Faculty Approval", description = "Email-based faculty approve/reject")
public class FacultyApprovalController {

    private final Dispatcher dispatcher;
    private final HtmlResponseService htmlService;

    @GetMapping("/approve/{id}")
    public ResponseEntity<String> approveFacultyForm(@PathVariable UUID id) {
        return ResponseEntity.ok()
            .contentType(MediaType.TEXT_HTML)
            .body(htmlService.buildConfirmationPage("Approve Faculty", "Are you sure you want to approve this faculty?", "/api/faculty-action/approve/" + id, "#16A34A"));
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<String> approveFaculty(@PathVariable UUID id) {
        Result<UUID> result = dispatcher.send(new ApproveFacultyCommand(id));
        return handleActionResult(result, "Faculty Approved", "The faculty has been approved successfully.", "#16A34A");
    }

    @GetMapping("/reject/{id}")
    public ResponseEntity<String> rejectFacultyForm(@PathVariable UUID id) {
        return ResponseEntity.ok()
            .contentType(MediaType.TEXT_HTML)
            .body(htmlService.buildConfirmationPage("Reject Faculty", "Are you sure you want to reject this faculty request?", "/api/faculty-action/reject/" + id, "#DC2626"));
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<String> rejectFaculty(@PathVariable UUID id) {
        Result<Void> result = dispatcher.send(new RejectFacultyCommand(id));
        return handleActionResult(result, "Faculty Rejected", "The faculty request has been rejected.", "#DC2626");
    }

    private ResponseEntity<String> handleActionResult(Result<?> result, String title, String successMessage, String color) {
        if (result.isFailure()) {
            return ResponseEntity.badRequest()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlService.buildPage("Error", result.getError().message(), "#DC2626"));
        }
        return ResponseEntity.ok()
            .contentType(MediaType.TEXT_HTML)
            .body(htmlService.buildPage(title + " ✓", successMessage, color));
    }
}
