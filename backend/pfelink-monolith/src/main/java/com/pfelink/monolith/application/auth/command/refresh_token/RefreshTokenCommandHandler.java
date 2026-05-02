package com.pfelink.monolith.application.auth.command.refresh_token;

import com.pfelink.monolith.application.auth.dto.AuthResponseDTO;
import com.pfelink.monolith.domain.auth.entity.RefreshToken;
import com.pfelink.monolith.domain.auth.entity.User;
import com.pfelink.monolith.domain.auth.repository.IRefreshTokenRepository;
import com.pfelink.monolith.infrastructure.security.token.JwtService;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenCommandHandler
        implements ICommandHandler<RefreshTokenCommand, Result<AuthResponseDTO>> {

    private final IRefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public Result<AuthResponseDTO> handle(RefreshTokenCommand cmd) {
        var tokenOpt = refreshTokenRepository.findByToken(cmd.refreshToken());
        
        if (tokenOpt.isEmpty()) {
            return Result.failure(Error.failure("Auth.InvalidRefreshToken", "Refresh token is invalid or has been used"));
        }

        RefreshToken token = tokenOpt.get();
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            return Result.failure(Error.failure("Auth.RefreshTokenExpired", "Refresh token has expired. Please log in again."));
        }

        User user = token.getUser();
        String accessToken = jwtService.generateToken(user);
        
        // Rotate refresh token: delete old, create new
        refreshTokenRepository.delete(token);
        String newRefreshTokenString = UUID.randomUUID().toString();
        RefreshToken newRefreshToken = RefreshToken.builder()
                .user(user)
                .token(newRefreshTokenString)
                .expiryDate(Instant.now().plusMillis(jwtService.getRefreshTokenExpiration()))
                .build();
        refreshTokenRepository.save(newRefreshToken);

        return Result.success(AuthResponseDTO.of(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                user.getStatus(),
                accessToken,
                newRefreshTokenString
        ));
    }
}
