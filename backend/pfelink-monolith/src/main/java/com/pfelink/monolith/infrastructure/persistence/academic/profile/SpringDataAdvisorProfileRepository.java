package com.pfelink.monolith.infrastructure.persistence.academic.profile;

import com.pfelink.monolith.domain.academic.entity.profile.AdvisorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataAdvisorProfileRepository extends JpaRepository<AdvisorProfile, UUID> {
    boolean existsByUserId(UUID userId);
    Optional<AdvisorProfile> findByUserId(UUID userId);
}
