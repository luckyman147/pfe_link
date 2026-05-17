package com.pfelink.monolith.api.auth.admin;

import com.pfelink.monolith.application.academic.command.approve_faculty.ApproveFacultyCommand;
import com.pfelink.monolith.application.academic.command.reject_faculty.RejectFacultyCommand;
import com.pfelink.monolith.application.academic.query.get_pending_faculties.GetPendingFacultiesQuery;
import com.pfelink.monolith.shared.cqrs.Dispatcher;
import com.pfelink.monolith.infrastructure.api.ResponseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin operations: manage faculties")
public class AdminController {

    private final Dispatcher dispatcher;

    @GetMapping("/pending-faculties")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPendingFaculties(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
        return ResponseUtil.toResponse(dispatcher.query(new GetPendingFacultiesQuery(page, size)));
    }

    @PostMapping("/approve-faculty/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveFaculty(@PathVariable UUID id) {
        return ResponseUtil.toResponse(dispatcher.send(new ApproveFacultyCommand(id)));
    }

    @PostMapping("/reject-faculty/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejectFaculty(@PathVariable UUID id) {
        return ResponseUtil.toResponse(dispatcher.send(new RejectFacultyCommand(id)));
    }
}
