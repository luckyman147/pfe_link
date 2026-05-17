package com.pfelink.monolith.api.auth;

import com.pfelink.monolith.infrastructure.security.RefreshTokenHandler;
import com.pfelink.monolith.infrastructure.api.ResponseUtil;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST endpoint for refreshing OAuth2 access tokens.
 * Client sends refresh_token in request body, receives new access_token.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication - Token Refresh", description = "OAuth2 token refresh endpoint")
public class RefreshTokenController {

    private final RefreshTokenHandler refreshTokenHandler;

    @PostMapping("/refresh-oauth2")
    @Operation(summary = "Refresh OAuth2 access token using refresh token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            RefreshTokenHandler.TokenResponse response = refreshTokenHandler.refreshAccessToken(request.refreshToken());

            Map<String, Object> data = Map.of(
                "access_token", response.accessToken(),
                "expires_in", response.expiresIn(),
                "token_type", "Bearer"
            );

            return ResponseEntity.ok(new Result<>(true, data, null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new Result<>(false, null, Error.failure("Auth.TokenRefreshFailed", e.getMessage())));
        }
    }

    /**
     * Request payload for token refresh.
     */
    public record RefreshTokenRequest(String refreshToken) {}
}
