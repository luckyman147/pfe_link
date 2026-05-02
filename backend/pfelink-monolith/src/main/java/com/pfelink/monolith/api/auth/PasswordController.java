package com.pfelink.monolith.api.auth;

import com.pfelink.monolith.application.auth.command.forgot_password.ForgotPasswordCommand;
import com.pfelink.monolith.application.auth.command.reset_password.ResetPasswordCommand;
import com.pfelink.monolith.application.auth.command.verify_otp.VerifyOtpCommand;
import com.pfelink.monolith.application.auth.dto.request.ForgotPasswordRequest;
import com.pfelink.monolith.application.auth.dto.request.ResetPasswordRequest;
import com.pfelink.monolith.application.auth.dto.request.VerifyOtpRequest;
import com.pfelink.monolith.infrastructure.api.ResponseUtil;
import com.pfelink.monolith.shared.cqrs.Dispatcher;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication - Password", description = "Password recovery and reset")
public class PasswordController {

    private final Dispatcher dispatcher;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest req) {
        return ResponseUtil.toResponse(dispatcher.send(new ForgotPasswordCommand(req.email())));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpRequest req) {
        return ResponseUtil.toResponse(dispatcher.send(new VerifyOtpCommand(req.email(), req.otpCode())));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest req) {
        return ResponseUtil.toResponse(dispatcher.send(new ResetPasswordCommand(
            req.email(), req.otpCode(), req.newPassword()
        )));
    }
}
