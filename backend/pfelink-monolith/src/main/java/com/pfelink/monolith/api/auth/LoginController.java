package com.pfelink.monolith.api.auth;

import com.pfelink.monolith.application.auth.command.login.LoginCommand;
import com.pfelink.monolith.application.auth.command.refresh_token.RefreshTokenCommand;
import com.pfelink.monolith.application.auth.dto.AuthResponseDTO;
import com.pfelink.monolith.application.auth.dto.TokenResponseDTO;
import com.pfelink.monolith.application.auth.dto.request.authentication.LoginRequest;
import com.pfelink.monolith.application.auth.dto.request.tokens.RefreshTokenRequest;
import com.pfelink.monolith.application.auth.query.get_me.GetMeQuery;
import com.pfelink.monolith.domain.auth.entity.User;
import com.pfelink.monolith.infrastructure.api.ResponseUtil;
import com.pfelink.monolith.infrastructure.security.util.CookieUtil;
import com.pfelink.monolith.shared.cqrs.Dispatcher;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication - Login", description = "Login, Logout, Session management")
public class LoginController {

    private final Dispatcher dispatcher;
    private final CookieUtil cookieUtil;
    private final LoginTokenHandler tokenHandler;

    @GetMapping("/me")
    @Operation(summary = "Get the current logged in user's profile")
    public ResponseEntity<?> getMe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            return ResponseEntity.status(401).body("User not authenticated");
        }
        return ResponseUtil.toResponse(dispatcher.query(new GetMeQuery(user.getId())));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req, HttpServletResponse response) {
        Result<AuthResponseDTO> result = dispatcher.send(new LoginCommand(req.email(), req.password()));
        if (result.isSuccess()) {
            cookieUtil.setTokenCookies(response, result.getValue().token(), result.getValue().refreshToken());
        }
        return ResponseUtil.toResponse(result);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        tokenHandler.blacklistCurrentToken(request);
        cookieUtil.clearTokenCookies(response);
        return ResponseEntity.ok().body("Logged out successfully");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody(required = false) RefreshTokenRequest req,
                                   @CookieValue(name = "refresh_token", required = false) String cookieRefreshToken,
                                   HttpServletResponse response) {
        String refreshToken = (req != null && req.refreshToken() != null) ? req.refreshToken() : cookieRefreshToken;
        if (refreshToken == null) {
            return ResponseUtil.toResponse(Result.failure(Error.validation("Refresh token is missing")));
        }
        Result<TokenResponseDTO> result = dispatcher.send(new RefreshTokenCommand(refreshToken));
        if (result.isSuccess()) {
            cookieUtil.setTokenCookies(response, result.getValue().accessToken(), result.getValue().refreshToken());
        }
        return ResponseUtil.toResponse(result);
    }
}
