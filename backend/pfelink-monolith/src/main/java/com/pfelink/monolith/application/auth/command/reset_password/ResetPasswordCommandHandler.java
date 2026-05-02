package com.pfelink.monolith.application.auth.command.reset_password;

import com.pfelink.monolith.domain.auth.entity.PasswordResetOtp;
import com.pfelink.monolith.domain.auth.entity.User;
import com.pfelink.monolith.domain.auth.repository.IOtpRepository;
import com.pfelink.monolith.domain.auth.repository.IUserRepository;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResetPasswordCommandHandler
        implements ICommandHandler<ResetPasswordCommand, Result<Void>> {

    private final IUserRepository userRepository;
    private final IOtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Result<Void> handle(ResetPasswordCommand cmd) {
        User user = userRepository.findByEmail(cmd.email()).orElse(null);
        if (user == null) {
            return Result.failure(Error.failure("User.NotFound", "No user found with this email"));
        }

        PasswordResetOtp otp = otpRepository.findByUserIdAndOtpCodeAndUsedFalse(user.getId(), cmd.otp()).orElse(null);
        if (otp == null || !otp.isValid()) {
            return Result.failure(Error.failure("Auth.InvalidOtp", "Invalid or expired OTP"));
        }

        otp.setUsed(true);
        otpRepository.save(otp);

        user.setPassword(passwordEncoder.encode(cmd.newPassword()));
        userRepository.save(user);
        return Result.success(null);
    }
}
