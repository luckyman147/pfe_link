package com.pfelink.monolith.application.auth.command.login;

import com.pfelink.monolith.application.auth.dto.AuthResponseDTO;
import com.pfelink.monolith.domain.auth.entity.User;
import com.pfelink.monolith.domain.auth.enums.AccountStatus;
import com.pfelink.monolith.domain.auth.entity.RefreshToken;
import com.pfelink.monolith.domain.auth.repository.IRefreshTokenRepository;
import com.pfelink.monolith.domain.auth.repository.IUserRepository;
import com.pfelink.monolith.infrastructure.security.token.JwtService;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginCommandHandler
        implements ICommandHandler<LoginCommand, Result<AuthResponseDTO>> {

    private final IUserRepository userRepository;
    private final IRefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public Result<AuthResponseDTO> handle(LoginCommand cmd) {
        User user = userRepository.findByEmail(cmd.email()).orElse(null);
        if (user == null || !passwordEncoder.matches(cmd.password(), user.getPassword())) {
            log.warn("Failed login attempt for email: {}", cmd.email());
            return Result.failure(Error.failure("Auth.InvalidCredentials", "Invalid email or password"));
        }

        if (!user.isEmailVerified()) {
            log.warn("Login attempt with unverified email: {}", cmd.email());
            return Result.failure(Error.failure("Auth.EmailNotVerified", "Please verify your email address before logging in."));
        }

        log.info("Successful login for user: {}", cmd.email());

        String token = jwtService.generateToken(user);
        String refreshTokenString = jwtService.generateRefreshToken(user);
        Instant expiryDate = Instant.now().plusMillis(jwtService.getRefreshTokenExpiration());

        // Handle refresh token with rotation (upsert) to avoid unique constraint violations
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .map(existing -> {
                    existing.setToken(refreshTokenString);
                    existing.setExpiryDate(expiryDate);
                    return existing;
                })
                .orElseGet(() -> RefreshToken.builder()
                        .user(user)
                        .token(refreshTokenString)
                        .expiryDate(expiryDate)
                        .build());

        refreshTokenRepository.save(refreshToken);

        return Result.success(AuthResponseDTO.of(
            user.getId(), user.getEmail(), user.getFullName(),
            user.getRole(), user.getStatus(), token, refreshTokenString
        ));
    }
}
