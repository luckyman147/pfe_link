package com.pfelink.monolith.application.auth.command.refresh_token;

import com.pfelink.monolith.application.auth.dto.TokenResponseDTO;
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

@Service
@RequiredArgsConstructor
public class RefreshTokenCommandHandler
        implements ICommandHandler<RefreshTokenCommand, Result<TokenResponseDTO>> {

    private final IRefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public Result<TokenResponseDTO> handle(RefreshTokenCommand cmd) {
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
        String newRefreshTokenString = jwtService.generateRefreshToken(user);

        // Update the existing token record instead of deleting/inserting to avoid unique constraint issues
        token.setToken(newRefreshTokenString);
        token.setExpiryDate(Instant.now().plusMillis(jwtService.getRefreshTokenExpiration()));
        refreshTokenRepository.save(token);

        return Result.success(TokenResponseDTO.of(accessToken, newRefreshTokenString));
    }
}
