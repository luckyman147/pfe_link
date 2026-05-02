package com.pfelink.monolith.api.academic.faculty.action;

import com.pfelink.monolith.application.academic.command.student.approve.ApproveStudentProfileCommand;
import com.pfelink.monolith.application.academic.command.student.reject.RejectStudentProfileCommand;
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
@Tag(name = "Student Approval", description = "Email-based student profile approve/reject")
public class StudentApprovalController {

    private final Dispatcher dispatcher;
    private final HtmlResponseService htmlService;

    @GetMapping("/approve-student")
    public ResponseEntity<String> approveStudentForm(@RequestParam UUID id) {
        return ResponseEntity.ok()
            .contentType(MediaType.TEXT_HTML)
            .body(htmlService.buildConfirmationPage("Approve Student", "Confirm student profile approval?", "/api/faculty-action/approve-student?id=" + id, "#16A34A"));
    }

    @PostMapping("/approve-student")
    public ResponseEntity<String> approveStudent(@RequestParam UUID id) {
        var result = dispatcher.send(new ApproveStudentProfileCommand(id));
        return handleActionResult(result, "Student Approved", "Student profile approved.", "#16A34A");
    }

    @GetMapping("/reject-student")
    public ResponseEntity<String> rejectStudentForm(@RequestParam UUID id) {
        return ResponseEntity.ok()
            .contentType(MediaType.TEXT_HTML)
            .body(htmlService.buildConfirmationPage("Reject Student", "Confirm student profile rejection?", "/api/faculty-action/reject-student?id=" + id, "#DC2626"));
    }

    @PostMapping("/reject-student")
    public ResponseEntity<String> rejectStudent(@RequestParam UUID id) {
        var result = dispatcher.send(new RejectStudentProfileCommand(id));
        return handleActionResult(result, "Student Rejected", "Student profile rejected.", "#DC2626");
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
