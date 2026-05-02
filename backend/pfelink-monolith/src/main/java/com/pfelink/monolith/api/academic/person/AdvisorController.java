package com.pfelink.monolith.api.academic.person;

import com.pfelink.monolith.application.academic.query.advisor.get_by_faculty.GetAdvisorsByFacultyQuery;
import com.pfelink.monolith.infrastructure.api.ResponseUtil;
import com.pfelink.monolith.shared.cqrs.Dispatcher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/advisors")
@RequiredArgsConstructor
@Tag(name = "Advisors", description = "Advisor discovery and profiles")
public class AdvisorController {

    private final Dispatcher dispatcher;

    @GetMapping("/faculty/{facultyId}")
    @Operation(summary = "Get all approved advisors for a faculty")
    public ResponseEntity<?> getAdvisorsByFaculty(@PathVariable UUID facultyId) {
        return ResponseUtil.toResponse(dispatcher.query(new GetAdvisorsByFacultyQuery(facultyId)));
    }
}
