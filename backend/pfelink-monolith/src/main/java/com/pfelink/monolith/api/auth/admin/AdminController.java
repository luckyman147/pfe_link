package com.pfelink.monolith.api.auth.admin;

import com.pfelink.monolith.application.academic.command.approve_faculty.ApproveFacultyCommand;
import com.pfelink.monolith.application.academic.command.reject_faculty.RejectFacultyCommand;
import com.pfelink.monolith.application.academic.query.get_pending_faculties.GetPendingFacultiesQuery;
import com.pfelink.monolith.shared.cqrs.Dispatcher;
import com.pfelink.monolith.infrastructure.api.ResponseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin operations: manage faculties")
public class AdminController {

    private final Dispatcher dispatcher;

    @GetMapping("/pending-faculties")
    public ResponseEntity<?> getPendingFaculties() {
        return ResponseUtil.toResponse(dispatcher.query(new GetPendingFacultiesQuery()));
    }

    @PostMapping("/approve-faculty/{id}")
    public ResponseEntity<?> approveFaculty(@PathVariable UUID id) {
        return ResponseUtil.toResponse(dispatcher.send(new ApproveFacultyCommand(id)));
    }

    @PostMapping("/reject-faculty/{id}")
    public ResponseEntity<?> rejectFaculty(@PathVariable UUID id) {
        return ResponseUtil.toResponse(dispatcher.send(new RejectFacultyCommand(id)));
    }
}
