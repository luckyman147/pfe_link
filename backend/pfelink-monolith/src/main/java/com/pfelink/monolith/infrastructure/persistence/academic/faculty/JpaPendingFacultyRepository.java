package com.pfelink.monolith.infrastructure.persistence.academic.faculty;

import com.pfelink.monolith.domain.academic.entity.faculty.PendingFaculty;
import com.pfelink.monolith.domain.academic.repository.IPendingFacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaPendingFacultyRepository implements IPendingFacultyRepository {

    private final SpringDataPendingFacultyRepository springRepo;

    @Override public PendingFaculty save(PendingFaculty pf) { return springRepo.save(pf); }
    @Override public Optional<PendingFaculty> findById(UUID id) { return springRepo.findById(id); }
    @Override public Optional<PendingFaculty> findByName(String n) { return springRepo.findByName(n); }
    @Override public Optional<PendingFaculty> findByEmail(String e) { return springRepo.findByEmail(e); }
    @Override public void deleteById(UUID id) { springRepo.deleteById(id); }
    @Override public List<PendingFaculty> findAll() { return springRepo.findAll(); }
    @Override public Page<PendingFaculty> findAll(Pageable pageable) { return springRepo.findAll(pageable); }
}
