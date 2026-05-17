package com.pfelink.monolith.domain.auth.repository;

import com.pfelink.monolith.domain.auth.entity.RefreshToken;
import com.pfelink.monolith.domain.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);

    @Modifying(flushAutomatically = true)
    int deleteByUser(User user);
}
