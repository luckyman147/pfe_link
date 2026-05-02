package com.pfelink.monolith.domain.auth.repository;

import com.pfelink.monolith.domain.auth.entity.PasswordResetOtp;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface IOtpRepository {
    PasswordResetOtp save(PasswordResetOtp otp);
    Optional<PasswordResetOtp> findByUserIdAndOtpCodeAndUsedFalse(UUID userId, String otpCode);
    Optional<PasswordResetOtp> findLatestUnusedByUserId(UUID userId);
    void deleteExpiredOtps(LocalDateTime now);
    void invalidateAllUserOtps(UUID userId);
}
