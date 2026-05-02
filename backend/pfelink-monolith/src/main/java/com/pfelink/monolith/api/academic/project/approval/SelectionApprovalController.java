package com.pfelink.monolith.api.academic.project.approval;

import com.pfelink.monolith.application.academic.command.selection.approve.ApproveSelectionRequestCommand;
import com.pfelink.monolith.application.academic.command.selection.reject.RejectSelectionRequestCommand;
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
@Tag(name = "Selection Approval", description = "Email-based selection request approve/reject")
public class SelectionApprovalController {

    private final Dispatcher dispatcher;
    private final HtmlResponseService htmlService;

    @GetMapping("/approve-selection")
    public ResponseEntity<String> approveSelectionForm(@RequestParam UUID id) {
        return ResponseEntity.ok()
            .contentType(MediaType.TEXT_HTML)
            .body(htmlService.buildConfirmationPage("Approve Selection", "Confirm selection request approval?", "/api/faculty-action/approve-selection?id=" + id, "#16A34A"));
    }

    @PostMapping("/approve-selection")
    public ResponseEntity<String> approveSelection(@RequestParam UUID id) {
        var result = dispatcher.send(new ApproveSelectionRequestCommand(id));
        return handleActionResult(result, "Selection Approved", "Selection request approved.", "#16A34A");
    }

    @GetMapping("/reject-selection")
    public ResponseEntity<String> rejectSelectionForm(@RequestParam UUID id) {
        return ResponseEntity.ok()
            .contentType(MediaType.TEXT_HTML)
            .body(htmlService.buildConfirmationPage("Reject Selection", "Confirm selection request rejection?", "/api/faculty-action/reject-selection?id=" + id, "#DC2626"));
    }

    @PostMapping("/reject-selection")
    public ResponseEntity<String> rejectSelection(@RequestParam UUID id) {
        var result = dispatcher.send(new RejectSelectionRequestCommand(id));
        return handleActionResult(result, "Selection Rejected", "Selection request rejected.", "#DC2626");
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
