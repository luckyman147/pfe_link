package com.pfelink.monolith.application.auth.command.verify_otp;

import com.pfelink.monolith.domain.auth.entity.PasswordResetOtp;
import com.pfelink.monolith.domain.auth.entity.User;
import com.pfelink.monolith.domain.auth.repository.IOtpRepository;
import com.pfelink.monolith.domain.auth.repository.IUserRepository;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VerifyOtpCommandHandler
        implements ICommandHandler<VerifyOtpCommand, Result<Void>> {

    private final IUserRepository userRepository;
    private final IOtpRepository otpRepository;

    @Override
    @Transactional(readOnly = true)
    public Result<Void> handle(VerifyOtpCommand cmd) {
        User user = userRepository.findByEmail(cmd.email()).orElse(null);
        if (user == null) {
            return Result.failure(Error.failure("User.NotFound", "No user found with this email"));
        }

        PasswordResetOtp otp = otpRepository.findByUserIdAndOtpCodeAndUsedFalse(user.getId(), cmd.otpCode()).orElse(null);
        if (otp == null || !otp.isValid()) {
            return Result.failure(Error.failure("Auth.InvalidOtp", "Invalid or expired OTP"));
        }

        return Result.success(null);
    }
}
