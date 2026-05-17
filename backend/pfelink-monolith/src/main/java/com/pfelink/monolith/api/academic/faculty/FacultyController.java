package com.pfelink.monolith.api.academic.faculty;

import com.pfelink.monolith.application.academic.command.create_faculty.CreateFacultyCommand;
import com.pfelink.monolith.application.academic.dto.request.CreateFacultyRequest;
import com.pfelink.monolith.application.academic.query.get_all_faculties.GetAllFacultiesQuery;
import com.pfelink.monolith.shared.result.Result;
import java.util.UUID;
import com.pfelink.monolith.shared.cqrs.Dispatcher;
import com.pfelink.monolith.infrastructure.api.ResponseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/faculties")
@RequiredArgsConstructor
@Tag(name = "Faculties", description = "Faculty management")
public class FacultyController {

    private final Dispatcher dispatcher;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllFaculties(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
        return ResponseUtil.toResponse(dispatcher.query(new GetAllFacultiesQuery(page, size)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createFaculty(@Valid @RequestBody CreateFacultyRequest req) {
        Result<UUID> result = dispatcher.send(new CreateFacultyCommand(
            req.name(), 
            req.abbreviation(), 
            req.email(),
            req.websiteUrl(), 
            req.imageUrl(), 
            req.adminPassword(),
            req.path()
        ));
        return ResponseUtil.toResponse(result);
    }
}
