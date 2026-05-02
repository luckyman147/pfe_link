package com.pfelink.monolith.domain.academic.repository;

import com.pfelink.monolith.domain.academic.entity.profile.AdvisorProfile;

import java.util.Optional;
import java.util.UUID;

public interface IAdvisorProfileRepository {
    AdvisorProfile save(AdvisorProfile advisorProfile);
    Optional<AdvisorProfile> findById(UUID id);
    boolean existsByUserId(UUID userId);
    Optional<AdvisorProfile> findByUserId(UUID userId);
}
