package com.pfelink.monolith.infrastructure.persistence.academic.profile;

import com.pfelink.monolith.domain.academic.entity.profile.StudentProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataStudentProfileRepository extends JpaRepository<StudentProfile, UUID> {
    boolean existsByUserId(UUID userId);
    Optional<StudentProfile> findByUserId(UUID userId);
    List<StudentProfile> findByStatus(com.pfelink.monolith.domain.academic.enums.student.StudentStatus status);
    List<StudentProfile> findByEmailContainingIgnoreCase(String email, Pageable pageable);
}
