package com.pfelink.monolith.infrastructure.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Extracts and maps claims from Microsoft Entra External ID tokens.
 * Handles oid (object ID), email, and roles → Spring Security authorities.
 */
@Component
public class EntraClaimsExtractor {

    private static final String OID_CLAIM = "oid";
    private static final String EMAIL_CLAIM = "email";
    private static final String ROLES_CLAIM = "roles";

    /**
     * Extract the Entra object ID (oid claim) from the token.
     * @param jwt the JWT token from Microsoft Entra
     * @return the oid value
     * @throws IllegalArgumentException if oid is missing
     */
    public String extractOid(Jwt jwt) {
        String oid = jwt.getClaimAsString(OID_CLAIM);
        if (oid == null || oid.isBlank()) {
            throw new IllegalArgumentException("Token missing required 'oid' claim");
        }
        return oid;
    }

    /**
     * Extract the email claim from the token.
     * @param jwt the JWT token from Microsoft Entra
     * @return the email value, or null if not present
     */
    public String extractEmail(Jwt jwt) {
        return jwt.getClaimAsString(EMAIL_CLAIM);
    }

    /**
     * Extract roles from the token and map to Spring Security GrantedAuthority objects.
     * Role names are prefixed with "ROLE_" per Spring convention.
     * @param jwt the JWT token from Microsoft Entra
     * @return set of GrantedAuthority (empty set if no roles present)
     */
    public Set<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Collection<String> roles = jwt.getClaimAsStringList(ROLES_CLAIM);

        if (roles == null || roles.isEmpty()) {
            return new HashSet<>();
        }

        return roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toSet());
    }
}
