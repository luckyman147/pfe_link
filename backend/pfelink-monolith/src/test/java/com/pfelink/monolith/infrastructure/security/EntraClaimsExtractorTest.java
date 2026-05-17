package com.pfelink.monolith.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EntraClaimsExtractorTest {

    private EntraClaimsExtractor extractor;

    @BeforeEach
    void setUp() {
        extractor = new EntraClaimsExtractor();
    }

    @Test
    void shouldExtractOidFromToken() {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put("oid", "12345678-1234-1234-1234-123456789012");
        claims.put("email", "user@example.com");
        claims.put("roles", Collections.singletonList("User"));

        Jwt jwt = mockJwt(claims);

        // Act
        String oid = extractor.extractOid(jwt);

        // Assert
        assertThat(oid).isEqualTo("12345678-1234-1234-1234-123456789012");
    }

    @Test
    void shouldExtractEmailFromToken() {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put("oid", "12345678-1234-1234-1234-123456789012");
        claims.put("email", "user@example.com");
        claims.put("roles", Collections.singletonList("User"));

        Jwt jwt = mockJwt(claims);

        // Act
        String email = extractor.extractEmail(jwt);

        // Assert
        assertThat(email).isEqualTo("user@example.com");
    }

    @Test
    void shouldExtractRolesAndMapToAuthorities() {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put("oid", "12345678-1234-1234-1234-123456789012");
        claims.put("email", "user@example.com");
        claims.put("roles", java.util.List.of("Admin", "Faculty"));

        Jwt jwt = mockJwt(claims);

        // Act
        Set<GrantedAuthority> authorities = extractor.extractAuthorities(jwt);

        // Assert
        assertThat(authorities)
            .extracting(GrantedAuthority::getAuthority)
            .containsExactlyInAnyOrder("ROLE_Admin", "ROLE_Faculty");
    }

    @Test
    void shouldThrowExceptionWhenOidMissing() {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", "user@example.com");

        Jwt jwt = mockJwt(claims);

        // Act & Assert
        assertThatThrownBy(() -> extractor.extractOid(jwt))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("oid");
    }

    @Test
    void shouldHandleEmptyRolesGracefully() {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put("oid", "12345678-1234-1234-1234-123456789012");
        claims.put("email", "user@example.com");
        claims.put("roles", Collections.emptyList());

        Jwt jwt = mockJwt(claims);

        // Act
        Set<GrantedAuthority> authorities = extractor.extractAuthorities(jwt);

        // Assert
        assertThat(authorities).isEmpty();
    }

    @Test
    void shouldHandleNullRolesGracefully() {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put("oid", "12345678-1234-1234-1234-123456789012");
        claims.put("email", "user@example.com");

        Jwt jwt = mockJwt(claims);

        // Act
        Set<GrantedAuthority> authorities = extractor.extractAuthorities(jwt);

        // Assert
        assertThat(authorities).isEmpty();
    }

    private Jwt mockJwt(Map<String, Object> claims) {
        return Jwt.withTokenValue("mock-token-value")
            .header("alg", "RS256")
            .header("typ", "JWT")
            .claims(c -> c.putAll(claims))
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .issuer("https://ciamlogin.com/12345678-1234-1234-1234-123456789012/v2.0")
            .subject("user-subject")
            .build();
    }
}
