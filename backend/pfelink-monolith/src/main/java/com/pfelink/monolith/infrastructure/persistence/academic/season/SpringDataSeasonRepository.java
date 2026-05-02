package com.pfelink.monolith.infrastructure.persistence.academic.season;

import com.pfelink.monolith.domain.academic.entity.season.Season;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataSeasonRepository extends JpaRepository<Season, UUID> {
    Optional<Season> findByIsActiveTrue();
}
