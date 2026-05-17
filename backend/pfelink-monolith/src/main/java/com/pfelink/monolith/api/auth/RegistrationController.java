package com.pfelink.monolith.api.auth;

import com.pfelink.monolith.application.auth.command.register_advisor.RegisterAdvisorCommand;
import com.pfelink.monolith.application.auth.command.register_student.RegisterStudentCommand;
import com.pfelink.monolith.application.auth.command.verify_email.VerifyEmailCommand;
import com.pfelink.monolith.application.auth.dto.request.authentication.RegisterAdvisorRequest;
import com.pfelink.monolith.application.auth.dto.request.authentication.RegisterStudentRequest;
import com.pfelink.monolith.infrastructure.api.ResponseUtil;
import com.pfelink.monolith.shared.cqrs.Dispatcher;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication - Registration", description = "User signup and email verification")
public class RegistrationController {

    private final Dispatcher dispatcher;

    @PostMapping("/signup/student")
    public ResponseEntity<?> registerStudent(@Valid @RequestBody RegisterStudentRequest req) {
        return ResponseUtil.toResponse(dispatcher.send(RegisterStudentCommand.of(
            req.email(), req.password(), req.fullName(),
            req.telephone(), req.cinNumber(),
            req.studentCardUrl(), req.draftId(), req.facultyId()
        )));
    }

    @PostMapping("/signup/advisor")
    public ResponseEntity<?> registerAdvisor(@Valid @RequestBody RegisterAdvisorRequest req) {
        return ResponseUtil.toResponse(dispatcher.send(RegisterAdvisorCommand.of(
            req.email(), req.password(), req.fullName(),
            req.telephone(), req.cinNumber(), req.cinCardUrl(), req.draftId(),
            req.facultyId(), req.facultyDomainEmail()
        )));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        return ResponseUtil.toResponse(dispatcher.send(new VerifyEmailCommand(token)));
    }
}
