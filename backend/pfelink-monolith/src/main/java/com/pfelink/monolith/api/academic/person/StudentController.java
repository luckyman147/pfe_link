package com.pfelink.monolith.api.academic.person;

import com.pfelink.monolith.domain.auth.entity.User;
import com.pfelink.monolith.application.academic.query.student.get_me.GetMyStudentProfileQuery;
import com.pfelink.monolith.application.academic.query.student.search.SearchStudentsQuery;
import com.pfelink.monolith.infrastructure.api.ResponseUtil;
import com.pfelink.monolith.shared.cqrs.Dispatcher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Tag(name = "Students", description = "Student profile and management")
public class StudentController {

    private final Dispatcher dispatcher;

    @GetMapping("/me")
    @Operation(summary = "Get the current logged in student's profile")
    public ResponseEntity<?> getMyProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        return ResponseUtil.toResponse(dispatcher.query(new GetMyStudentProfileQuery(user.getId())));
    }

    @GetMapping("/search")
    @Operation(summary = "Search for students by email")
    public ResponseEntity<?> searchStudents(@RequestParam String email) {
        return ResponseUtil.toResponse(dispatcher.query(new SearchStudentsQuery(email)));
    }
}
