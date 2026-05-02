package com.pfelink.monolith.infrastructure.persistence.auth;

import com.pfelink.monolith.domain.auth.entity.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataOtpRepository extends JpaRepository<PasswordResetOtp, UUID> {
    Optional<PasswordResetOtp> findByUserIdAndOtpCodeAndUsedFalse(UUID userId, String otpCode);
    Optional<PasswordResetOtp> findByUserIdAndUsedFalseOrderByCreatedAtDesc(UUID userId);

    @Modifying
    @Query("DELETE FROM PasswordResetOtp p WHERE p.expiresAt < :now")
    void deleteExpiredOtps(LocalDateTime now);

    @Modifying
    @Query("UPDATE PasswordResetOtp p SET p.used = true WHERE p.userId = :userId AND p.used = false")
    void invalidateAllUserOtps(UUID userId);
}
