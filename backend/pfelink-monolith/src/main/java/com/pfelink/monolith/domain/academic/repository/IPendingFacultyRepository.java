package com.pfelink.monolith.domain.academic.repository;

import com.pfelink.monolith.domain.academic.entity.faculty.PendingFaculty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPendingFacultyRepository {
    PendingFaculty save(PendingFaculty pendingFaculty);
    Optional<PendingFaculty> findById(UUID id);
    Optional<PendingFaculty> findByName(String name);
    Optional<PendingFaculty> findByEmail(String email);
    void deleteById(UUID id);
    List<PendingFaculty> findAll();
    Page<PendingFaculty> findAll(Pageable pageable);
}
