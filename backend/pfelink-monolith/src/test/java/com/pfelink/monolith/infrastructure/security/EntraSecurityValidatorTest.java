package com.pfelink.monolith.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EntraSecurityValidatorTest {

    private EntraSecurityValidator validator;

    @BeforeEach
    void setUp() {
        validator = new EntraSecurityValidator();
    }

    @Test
    void shouldValidateTokenIssuer() {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put("oid", "12345678-1234-1234-1234-123456789012");

        Jwt jwt = Jwt.withTokenValue("token")
            .issuer("https://ciamlogin.com/tenant-id/v2.0")
            .claims(c -> c.putAll(claims))
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build();

        // Act
        boolean isValid = validator.validateIssuer(jwt, "https://ciamlogin.com/tenant-id/v2.0");

        // Assert
        assertThat(isValid).isTrue();
    }

    @Test
    void shouldRejectTokenFromWrongIssuer() {
        // Arrange
        Jwt jwt = Jwt.withTokenValue("token")
            .issuer("https://wrong-issuer.com/v2.0")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build();

        // Act & Assert
        assertThatThrownBy(() -> validator.validateIssuer(jwt, "https://ciamlogin.com/tenant-id/v2.0"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("issuer");
    }

    @Test
    void shouldCheckTokenNotExpired() {
        // Arrange
        Jwt expiredToken = Jwt.withTokenValue("token")
            .issuer("https://ciamlogin.com/tenant-id/v2.0")
            .issuedAt(Instant.now().minusSeconds(7200))
            .expiresAt(Instant.now().minusSeconds(3600))
            .build();

        // Act & Assert
        assertThatThrownBy(() -> validator.validateExpiry(expiredToken))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("expired");
    }

    @Test
    void shouldAllowValidToken() {
        // Arrange
        Jwt validToken = Jwt.withTokenValue("token")
            .issuer("https://ciamlogin.com/tenant-id/v2.0")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build();

        // Act & Assert (no exception)
        validator.validateExpiry(validToken);
    }

    @Test
    void shouldValidateAudience() {
        // Arrange
        Jwt jwt = Jwt.withTokenValue("token")
            .issuer("https://ciamlogin.com/tenant-id/v2.0")
            .claim("aud", "client-id")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build();

        // Act
        boolean isValid = validator.validateAudience(jwt, "client-id");

        // Assert
        assertThat(isValid).isTrue();
    }

    @Test
    void shouldRejectWrongAudience() {
        // Arrange
        Jwt jwt = Jwt.withTokenValue("token")
            .issuer("https://ciamlogin.com/tenant-id/v2.0")
            .claim("aud", "wrong-client-id")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build();

        // Act & Assert
        assertThatThrownBy(() -> validator.validateAudience(jwt, "expected-client-id"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("audience");
    }

    @Test
    void shouldValidateOidPresent() {
        // Arrange
        Jwt jwt = Jwt.withTokenValue("token")
            .issuer("https://ciamlogin.com/tenant-id/v2.0")
            .claim("oid", "12345678-1234-1234-1234-123456789012")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build();

        // Act
        boolean isValid = validator.validateOidPresent(jwt);

        // Assert
        assertThat(isValid).isTrue();
    }

    @Test
    void shouldRejectTokenWithoutOid() {
        // Arrange
        Jwt jwt = Jwt.withTokenValue("token")
            .issuer("https://ciamlogin.com/tenant-id/v2.0")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build();

        // Act & Assert
        assertThatThrownBy(() -> validator.validateOidPresent(jwt))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("oid");
    }
}
