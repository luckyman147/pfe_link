package com.pfelink.monolith.infrastructure.persistence.academic.profile;

import com.pfelink.monolith.domain.academic.entity.profile.StudentProfile;
import com.pfelink.monolith.domain.academic.enums.StudentStatus;
import com.pfelink.monolith.domain.academic.repository.IStudentProfileRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaStudentProfileRepository implements IStudentProfileRepository {

    private final SpringDataStudentProfileRepository springRepo;

    @Override
    public StudentProfile save(StudentProfile sp) {
        return springRepo.save(sp);
    }

    @Override
    public Optional<StudentProfile> findById(UUID id) {
        return springRepo.findById(id);
    }

    @Override
    public boolean existsByUserId(UUID uid) {
        return springRepo.existsByUserId(uid);
    }

    @Override
    public Optional<StudentProfile> findByUserId(UUID uid) {
        return springRepo.findByUserId(uid);
    }

    @Override
    public List<StudentProfile> findAllPending() {
        return springRepo.findByStatus(StudentStatus.PENDING);
    }

    @Override
    public List<StudentProfile> findByEmailContainingIgnoreCase(String email, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByEmailContainingIgnoreCase'");
    }
}
