package com.pfelink.monolith.infrastructure.persistence.academic.season;

import com.pfelink.monolith.domain.academic.entity.season.Season;
import com.pfelink.monolith.domain.academic.repository.ISeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaSeasonRepository implements ISeasonRepository {

    private final SpringDataSeasonRepository springRepo;

    @Override public Season save(Season s) { return springRepo.save(s); }
    @Override public Optional<Season> findById(UUID id) { return springRepo.findById(id); }
    @Override public Optional<Season> findActiveSeason() { return springRepo.findByIsActiveTrue(); }
}
