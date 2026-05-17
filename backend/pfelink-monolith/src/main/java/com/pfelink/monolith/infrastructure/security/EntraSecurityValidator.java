package com.pfelink.monolith.infrastructure.security;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Additional security validation for Microsoft Entra tokens.
 * Spring Security's OAuth2 Resource Server handles standard JWT validation;
 * this component adds application-specific checks.
 *
 * Best practices:
 * 1. Always validate issuer matches your Entra tenant
 * 2. Verify token not expired (Spring does this, but good to double-check)
 * 3. Ensure audience (aud) claim matches your application client ID
 * 4. Log suspicious tokens for security audits
 */
@Component
public class EntraSecurityValidator {

    /**
     * Validate the token issuer matches the expected Microsoft Entra issuer.
     * @param jwt the JWT token
     * @param expectedIssuer the expected issuer URI (e.g., https://ciamlogin.com/tenant-id/v2.0)
     * @throws IllegalArgumentException if issuer doesn't match
     */
    public boolean validateIssuer(Jwt jwt, String expectedIssuer) {
        String actualIssuer = jwt.getIssuer();

        if (actualIssuer == null || !actualIssuer.equals(expectedIssuer)) {
            throw new IllegalArgumentException(
                "Token issuer '" + actualIssuer + "' does not match expected issuer '" + expectedIssuer + "'"
            );
        }

        return true;
    }

    /**
     * Validate the token has not expired.
     * (Spring Security handles this automatically, but this is a defense-in-depth check.)
     * @param jwt the JWT token
     * @throws IllegalArgumentException if token is expired
     */
    public boolean validateExpiry(Jwt jwt) {
        Instant expiresAt = jwt.getExpiresAt();

        if (expiresAt == null || expiresAt.isBefore(Instant.now())) {
            throw new IllegalArgumentException("Token is expired");
        }

        return true;
    }

    /**
     * Validate the token audience (aud claim) matches the application client ID.
     * This ensures the token was issued for your application, not for another service.
     * @param jwt the JWT token
     * @param expectedAudience the expected client ID / audience
     * @throws IllegalArgumentException if audience doesn't match
     */
    public boolean validateAudience(Jwt jwt, String expectedAudience) {
        String actualAudience = jwt.getClaimAsString("aud");

        if (actualAudience == null || !actualAudience.equals(expectedAudience)) {
            throw new IllegalArgumentException(
                "Token audience '" + actualAudience + "' does not match application client ID '" + expectedAudience + "'"
            );
        }

        return true;
    }

    /**
     * Verify the 'oid' (object ID) claim is present.
     * This is a Microsoft-specific claim identifying the user in Entra.
     * @param jwt the JWT token
     * @throws IllegalArgumentException if 'oid' is missing
     */
    public boolean validateOidPresent(Jwt jwt) {
        String oid = jwt.getClaimAsString("oid");

        if (oid == null || oid.isBlank()) {
            throw new IllegalArgumentException("Token missing required 'oid' (object ID) claim from Microsoft Entra");
        }

        return true;
    }
}
