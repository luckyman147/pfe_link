package com.pfelink.monolith.infrastructure.persistence.academic.faculty;

import com.pfelink.monolith.domain.academic.entity.faculty.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataFacultyRepository extends JpaRepository<Faculty, UUID> {
    boolean existsByEmail(String email);
    boolean existsByName(String name);
    boolean existsByWebsiteUrl(String url);
    Optional<Faculty> findByEmail(String email);
}
