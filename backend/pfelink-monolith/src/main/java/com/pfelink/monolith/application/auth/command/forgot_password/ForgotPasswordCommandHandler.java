package com.pfelink.monolith.application.auth.command.forgot_password;

import com.pfelink.monolith.domain.auth.entity.PasswordResetOtp;
import com.pfelink.monolith.domain.auth.entity.User;
import com.pfelink.monolith.domain.auth.repository.IOtpRepository;
import com.pfelink.monolith.domain.auth.repository.IUserRepository;
import com.pfelink.monolith.infrastructure.event.events.auth.PasswordResetEvent;
import com.pfelink.monolith.infrastructure.security.service.OtpService;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ForgotPasswordCommandHandler
        implements ICommandHandler<ForgotPasswordCommand, Result<Void>> {

    private final IUserRepository userRepository;
    private final IOtpRepository otpRepository;
    private final OtpService otpService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Result<Void> handle(ForgotPasswordCommand cmd) {
        User user = userRepository.findByEmail(cmd.email()).orElse(null);
        if (user == null) {
            return Result.failure(Error.failure("User.NotFound", "No user found with this email"));
        }

        otpRepository.invalidateAllUserOtps(user.getId());
        String otp = otpService.generateOtp(5);

        PasswordResetOtp otpEntity = new PasswordResetOtp();
        otpEntity.setUserId(user.getId());
        otpEntity.setOtpCode(otp);
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        otpRepository.save(otpEntity);

        eventPublisher.publishEvent( PasswordResetEvent.of(user.getEmail(), user.getFullName(), otp));
        return Result.success(null);
    }
}
