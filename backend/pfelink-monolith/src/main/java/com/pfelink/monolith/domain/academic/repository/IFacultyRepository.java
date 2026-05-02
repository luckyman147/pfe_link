package com.pfelink.monolith.domain.academic.repository;

import com.pfelink.monolith.domain.academic.entity.faculty.Faculty;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IFacultyRepository {
    Faculty save(Faculty faculty);
    Optional<Faculty> findById(UUID id);
    boolean existsByEmail(String email);
    boolean existsByName(String name);
    boolean existsByWebsiteUrl(String websiteUrl);
    Optional<Faculty> findByEmail(String email);
    List<Faculty> findAll();
}
