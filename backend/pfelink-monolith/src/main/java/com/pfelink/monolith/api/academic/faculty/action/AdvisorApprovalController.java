package com.pfelink.monolith.api.academic.faculty.action;

import com.pfelink.monolith.application.academic.command.approve_faculty_assignment.ApproveFacultyAssignmentCommand;
import com.pfelink.monolith.application.academic.command.reject_faculty_assignment.RejectFacultyAssignmentCommand;
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
@Tag(name = "Advisor Approval", description = "Email-based advisor assignment approve/reject")
public class AdvisorApprovalController {

    private final Dispatcher dispatcher;
    private final HtmlResponseService htmlService;

    @GetMapping("/approve-advisor")
    public ResponseEntity<String> approveAdvisorForm(@RequestParam UUID advisorId, @RequestParam UUID facultyId) {
        String url = "/api/faculty-action/approve-advisor?advisorId=" + advisorId + "&facultyId=" + facultyId;
        return ResponseEntity.ok()
            .contentType(MediaType.TEXT_HTML)
            .body(htmlService.buildConfirmationPage("Approve Advisor", "Confirm advisor approval for your faculty?", url, "#16A34A"));
    }

    @PostMapping("/approve-advisor")
    public ResponseEntity<String> approveAdvisor(@RequestParam UUID advisorId, @RequestParam UUID facultyId) {
        var result = dispatcher.send(new ApproveFacultyAssignmentCommand(advisorId, facultyId));
        return handleActionResult(result, "Advisor Approved", "Successfully approved advisor.", "#16A34A");
    }

    @GetMapping("/reject-advisor")
    public ResponseEntity<String> rejectAdvisorForm(@RequestParam UUID advisorId, @RequestParam UUID facultyId) {
        String url = "/api/faculty-action/reject-advisor?advisorId=" + advisorId + "&facultyId=" + facultyId;
        return ResponseEntity.ok()
            .contentType(MediaType.TEXT_HTML)
            .body(htmlService.buildConfirmationPage("Reject Advisor", "Confirm advisor rejection?", url, "#DC2626"));
    }

    @PostMapping("/reject-advisor")
    public ResponseEntity<String> rejectAdvisor(@RequestParam UUID advisorId, @RequestParam UUID facultyId) {
        var result = dispatcher.send(new RejectFacultyAssignmentCommand(advisorId, facultyId));
        return handleActionResult(result, "Advisor Rejected", "Advisor request has been rejected.", "#DC2626");
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
