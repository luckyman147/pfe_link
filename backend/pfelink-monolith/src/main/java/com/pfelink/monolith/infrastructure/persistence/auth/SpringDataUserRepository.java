package com.pfelink.monolith.infrastructure.persistence.auth;

import com.pfelink.monolith.domain.auth.entity.User;
import com.pfelink.monolith.domain.auth.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailVerificationToken(String token);
    boolean existsByEmail(String email);
    boolean existsByTelephone(String telephone);
    List<User> findByRole(UserRole role);
    Page<User> findByRole(UserRole role, Pageable pageable);
}
