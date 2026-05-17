package com.pfelink.monolith.infrastructure.persistence.academic.faculty;

import com.pfelink.monolith.domain.academic.entity.faculty.Faculty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataFacultyRepository extends JpaRepository<Faculty, UUID> {
    boolean existsByEmail(String email);
    boolean existsByName(String name);
    boolean existsByWebsiteUrl(String url);
    Optional<Faculty> findByEmail(String email);

    @Query("SELECT f FROM Faculty f WHERE f.validated = true ORDER BY f.name ASC")
    List<Faculty> findAllValidated();

    @Query("SELECT f FROM Faculty f WHERE f.validated = true")
    Page<Faculty> findAllValidated(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Faculty f SET f.validated = true WHERE f.id IN :ids")
    int batchValidate(@Param("ids") List<UUID> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Faculty f SET f.validated = false WHERE f.id IN :ids")
    int batchInvalidate(@Param("ids") List<UUID> ids);
}
