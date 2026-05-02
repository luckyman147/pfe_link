package com.pfelink.monolith.infrastructure.persistence.academic.faculty;

import com.pfelink.monolith.domain.academic.entity.faculty.PendingFaculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataPendingFacultyRepository extends JpaRepository<PendingFaculty, UUID> {
    Optional<PendingFaculty> findByName(String name);
    Optional<PendingFaculty> findByEmail(String email);
}
