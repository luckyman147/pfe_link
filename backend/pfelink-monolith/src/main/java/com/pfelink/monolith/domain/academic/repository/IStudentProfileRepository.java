package com.pfelink.monolith.domain.academic.repository;

import com.pfelink.monolith.domain.academic.entity.profile.StudentProfile;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IStudentProfileRepository {
    StudentProfile save(StudentProfile studentProfile);
    Optional<StudentProfile> findById(UUID id);
    boolean existsByUserId(UUID userId);
    Optional<StudentProfile> findByUserId(UUID userId);
    List<StudentProfile> findAllPending();
    List<StudentProfile> findByEmailContainingIgnoreCase(String email, Pageable pageable);
}
