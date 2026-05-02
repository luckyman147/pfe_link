package com.pfelink.monolith.infrastructure.persistence.auth;

import com.pfelink.monolith.domain.auth.entity.PasswordResetOtp;
import com.pfelink.monolith.domain.auth.repository.IOtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaOtpRepository implements IOtpRepository {

    private final SpringDataOtpRepository springRepo;

    @Override public PasswordResetOtp save(PasswordResetOtp otp) { return springRepo.save(otp); }
    @Override public Optional<PasswordResetOtp> findByUserIdAndOtpCodeAndUsedFalse(UUID uid, String otp) { return springRepo.findByUserIdAndOtpCodeAndUsedFalse(uid, otp); }
    @Override public Optional<PasswordResetOtp> findLatestUnusedByUserId(UUID uid) { return springRepo.findByUserIdAndUsedFalseOrderByCreatedAtDesc(uid); }
    @Override public void deleteExpiredOtps(LocalDateTime now) { springRepo.deleteExpiredOtps(now); }
    @Override public void invalidateAllUserOtps(UUID userId) { springRepo.invalidateAllUserOtps(userId); }
}
