package com.pfelink.monolith.infrastructure.persistence.academic.faculty;

import com.pfelink.monolith.domain.academic.entity.faculty.Faculty;
import com.pfelink.monolith.domain.academic.repository.IFacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaFacultyRepository implements IFacultyRepository {

    private final SpringDataFacultyRepository springRepo;

    @Override public Faculty save(Faculty f) { return springRepo.save(f); }
    @Override public Optional<Faculty> findById(UUID id) { return springRepo.findById(id); }
    @Override public boolean existsByEmail(String e) { return springRepo.existsByEmail(e); }
    @Override public boolean existsByName(String n) { return springRepo.existsByName(n); }
    @Override public boolean existsByWebsiteUrl(String u) { return springRepo.existsByWebsiteUrl(u); }
    @Override public Optional<Faculty> findByEmail(String e) { return springRepo.findByEmail(e); }
    @Override public List<Faculty> findAll() { return springRepo.findAll(); }
}
