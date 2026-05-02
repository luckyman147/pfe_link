package com.pfelink.monolith.infrastructure.persistence.auth;

import com.pfelink.monolith.domain.auth.entity.User;
import com.pfelink.monolith.domain.auth.enums.UserRole;
import com.pfelink.monolith.domain.auth.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements IUserRepository {

    private final SpringDataUserRepository springRepo;

    @Override public User save(User user) { return springRepo.save(user); }
    @Override public Optional<User> findById(UUID id) { return springRepo.findById(id); }
    @Override public Optional<User> findByEmail(String e) { return springRepo.findByEmail(e); }
    @Override public Optional<User> findByEmailVerificationToken(String t) { return springRepo.findByEmailVerificationToken(t); }
    @Override public boolean existsByEmail(String e) { return springRepo.existsByEmail(e); }
    @Override public boolean existsByTelephone(String t) { return springRepo.existsByTelephone(t); }
    @Override public List<User> findByRole(UserRole r) { return springRepo.findByRole(r); }
}
