package com.pfelink.monolith.infrastructure.security;

import com.pfelink.monolith.domain.auth.entity.User;
import com.pfelink.monolith.domain.auth.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private static final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET);
        ReflectionTestUtils.setField(jwtService, "expiration", 3600000L);
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        // Arrange
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@test.com");
        user.setRole(UserRole.STUDENT);
        user.setFullName("Test User");

        // Act
        String token = jwtService.generateToken(user);

        // Assert
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token));
        assertEquals(user.getId().toString(), jwtService.extractSubject(token));
        assertEquals("test@test.com", jwtService.extractEmail(token));
    }

    @Test
    void isTokenValid_ShouldReturnFalseForInvalidToken() {
        // Act & Assert
        assertFalse(jwtService.isTokenValid("invalid.token.here"));
    }
}
