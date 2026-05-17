package com.pfelink.monolith.api.auth;

import com.pfelink.monolith.application.auth.query.get_me.GetMeQuery;
import com.pfelink.monolith.domain.auth.entity.User;
import com.pfelink.monolith.infrastructure.api.ResponseUtil;
import com.pfelink.monolith.shared.cqrs.Dispatcher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * OAuth2 session management endpoints.
 *
 * NOTE: Login is handled via Microsoft Entra External ID OAuth2 flow (ciamlogin.com).
 * Clients should:
 * 1. Redirect user to Microsoft Entra login page
 * 2. Receive access token + refresh token from Microsoft
 * 3. Use access token in Authorization header for API calls
 * 4. Call /api/auth/refresh-oauth2 with refresh token when access token expires
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication - Session", description = "Get current user, logout")
public class LoginController {

    private final Dispatcher dispatcher;

    @GetMapping("/me")
    @Operation(summary = "Get the current logged in user's profile")
    public ResponseEntity<?> getMe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            return ResponseEntity.status(401).body("User not authenticated");
        }
        return ResponseUtil.toResponse(dispatcher.query(new GetMeQuery(user.getId())));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout current user - clears security context")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().body("Logged out successfully");
    }

    /**
     * For token refresh via OAuth2, use POST /api/auth/refresh-oauth2
     * (see RefreshTokenController for OAuth2 token refresh endpoint)
     */

}
