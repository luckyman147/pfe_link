package com.pfelink.monolith.domain.academic.repository;

import com.pfelink.monolith.domain.academic.entity.season.Season;

import java.util.Optional;
import java.util.UUID;

public interface ISeasonRepository {
    Season save(Season season);
    Optional<Season> findById(UUID id);
    Optional<Season> findActiveSeason();
}
