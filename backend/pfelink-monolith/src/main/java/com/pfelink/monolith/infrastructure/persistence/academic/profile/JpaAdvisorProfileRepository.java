package com.pfelink.monolith.infrastructure.persistence.academic.profile;

import com.pfelink.monolith.domain.academic.entity.profile.AdvisorProfile;
import com.pfelink.monolith.domain.academic.repository.IAdvisorProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaAdvisorProfileRepository implements IAdvisorProfileRepository {

    private final SpringDataAdvisorProfileRepository springRepo;

    @Override public AdvisorProfile save(AdvisorProfile ap) { return springRepo.save(ap); }
    @Override public Optional<AdvisorProfile> findById(UUID id) { return springRepo.findById(id); }
    @Override public boolean existsByUserId(UUID uid) { return springRepo.existsByUserId(uid); }
    @Override public Optional<AdvisorProfile> findByUserId(UUID uid) { return springRepo.findByUserId(uid); }
}
