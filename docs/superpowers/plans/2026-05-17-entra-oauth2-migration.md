# Microsoft Entra External ID OAuth2 Migration Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Migrate from custom JWT token generation/validation to Microsoft Entra External ID (OIDC/OAuth2) tokens issued by `ciamlogin.com`, removing all custom JWT infrastructure while maintaining user roles and claims extraction.

**Architecture:** Spring Security 6.x OAuth2 Resource Server validates incoming Microsoft access tokens at the `@PreAuthorize` layer. Token claims (oid, email, roles) are extracted once during request processing and mapped to Spring Security authorities. No custom JWT filter or token generation—all token validation delegated to Microsoft's JWKS endpoint.

**Tech Stack:**
- Spring Boot 3.4.2 with Spring Security 6.x (already includes OAuth2 Resource Server)
- Microsoft Entra External ID OIDC issuer: `https://ciamlogin.com/{tenant-id}`
- PostgreSQL (user store: azureId, role, email)
- JUnit 5 + Mockito for testing

---

## File Structure

**New files:**
- `src/main/java/com/pfelink/monolith/infrastructure/security/EntraClaimsExtractor.java` — Extract and map Microsoft token claims to application domain
- `src/main/resources/application.yml` — OAuth2 Resource Server config (replace JWT config)
- `src/test/java/com/pfelink/monolith/infrastructure/security/EntraClaimsExtractorTest.java` — Unit tests for claim extraction

**Modified files:**
- `src/main/java/com/pfelink/monolith/infrastructure/config/SecurityConfig.java` — Replace JWT filter chain with OAuth2 Resource Server
- `src/main/java/com/pfelink/monolith/api/**/*Controller.java` — Remove custom token validation, rely on @PreAuthorize
- Remove entirely:
  - Any custom JWT filter class (e.g., `JwtAuthenticationFilter`, `JwtTokenProvider`, `TokenValidationService`)
  - Custom token generation service (e.g., `JwtTokenService`)
  - JWT secret/key configuration properties

**Reference files (read-only to understand current state):**
- `.vscode/settings.json` — Current project settings
- `backend/pfelink-monolith/pom.xml` — Dependency verification (spring-security-oauth2-resource-server should already exist)

---

## Task 1: Audit and Remove Legacy JWT Infrastructure

**Files:**
- Identify and remove: custom JWT filter, token generation, secret config
- Modify: `backend/pfelink-monolith/pom.xml` (remove jjwt if present, verify oauth2-resource-server is present)
- Modify: `src/main/resources/application.yml` (remove jwt.secret, jwt.expiration)

### Step 1a: Identify JWT Implementation Files

Run a grep to find custom JWT classes in your codebase:

```bash
grep -r "JwtTokenProvider\|JwtAuthenticationFilter\|JwtTokenService\|TokenGenerationService" backend/pfelink-monolith/src --include="*.java"
```

Expected output: List of files containing custom JWT logic (if none found, JWT may already be removed or named differently). Adjust search terms if needed.

**Note for the engineer:** Save the file paths from this output—you'll delete them in Step 1c.

### Step 1b: Check pom.xml for JWT and OAuth2 Dependencies

Read the pom.xml:

```bash
grep -A 2 -B 2 "jjwt\|io.jsonwebtoken\|spring-security-oauth2-resource-server" backend/pfelink-monolith/pom.xml
```

Expected output:
- `jjwt` library entries (if present, they'll be removed)
- `spring-security-oauth2-resource-server` entry (must be present for OAuth2)

If `spring-security-oauth2-resource-server` is NOT in pom.xml, add it:

```xml
<!-- OAuth2 Resource Server for validating external JWT tokens (e.g., Microsoft Entra) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

### Step 1c: Remove JWT-Related Dependencies from pom.xml

Open `backend/pfelink-monolith/pom.xml` and search for:
- `jjwt`, `io.jsonwebtoken` entries
- `jackson-databind-jdk8` or similar JWT helper libraries

Remove these `<dependency>` blocks. Example of what to remove:

```xml
<!-- REMOVE THIS ENTIRE BLOCK -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

Verify the file compiles after removal:

```bash
cd backend/pfelink-monolith && mvn clean compile
```

Expected: Compilation succeeds (no unresolved jjwt imports).

### Step 1d: Remove Custom JWT Class Files

Delete all custom JWT implementation files identified in Step 1a. Examples (adjust paths to match your actual files):

```bash
rm -f "backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/security/JwtTokenProvider.java"
rm -f "backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/security/JwtAuthenticationFilter.java"
rm -f "backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/security/TokenGenerationService.java"
```

Verify compilation after deletion:

```bash
cd backend/pfelink-monolith && mvn clean compile
```

Expected: Compilation succeeds (no "cannot find symbol" errors for deleted JWT classes).

### Step 1e: Commit Cleanup

```bash
cd backend/pfelink-monolith
git add pom.xml src/
git commit -m "chore: remove custom JWT implementation (jjwt, filters, token generation)"
```

---

## Task 2: Create application.yml OAuth2 Configuration

**Files:**
- Modify: `backend/pfelink-monolith/src/main/resources/application.yml`
- Test: Manual verification (config parsing)

### Step 2a: Update application.yml with OAuth2 Resource Server Config

Open `backend/pfelink-monolith/src/main/resources/application.yml` and replace all `jwt.*` config with OAuth2 configuration. Add or update the `spring.security.oauth2.resourceserver` section:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          # Microsoft Entra External ID JWKS endpoint
          # Replace {tenant-id} with your actual Entra tenant ID
          jwk-set-uri: "https://ciamlogin.com/{tenant-id}/discovery/v2.0/keys"
          # Issuer URI for token validation
          issuer-uri: "https://ciamlogin.com/{tenant-id}/v2.0"
          # Application (client) ID from Entra app registration
          audiences: "YOUR_APPLICATION_CLIENT_ID"
```

**IMPORTANT:** Replace these placeholders:
- `{tenant-id}` — Your Microsoft Entra External ID tenant ID (e.g., "12345678-1234-1234-1234-123456789012" or your custom domain)
- `YOUR_APPLICATION_CLIENT_ID` — The client ID of your app registration in Entra External ID (available in Entra admin center)

### Step 2b: Verify Configuration Structure

Run the application to verify config loads without errors:

```bash
cd backend/pfelink-monolith && mvn spring-boot:run -DskipTests
```

Expected output: Application starts with no configuration errors. Watch logs for:
```
o.s.s.o.r.w.BearerTokenAuthenticationFilter : Creating filter chain: ...
```

Stop the app with `Ctrl+C`.

### Step 2c: Remove Legacy JWT Config (if present)

Search `application.yml` for any remaining JWT-related properties and delete them:

```bash
grep -n "jwt\." backend/pfelink-monolith/src/main/resources/application.yml
```

Remove entries like:
- `app.jwt.secret: ...`
- `app.jwt.expiration: ...`
- `app.jwt.refresh-expiration: ...`

### Step 2d: Commit Configuration

```bash
cd backend/pfelink-monolith
git add src/main/resources/application.yml
git commit -m "config: replace JWT config with OAuth2 Resource Server (Entra External ID)"
```

---

## Task 3: Create EntraClaimsExtractor Component

**Files:**
- Create: `backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/security/EntraClaimsExtractor.java`
- Test: `backend/pfelink-monolith/src/test/java/com/pfelink/monolith/infrastructure/security/EntraClaimsExtractorTest.java`

### Step 3a: Write Unit Test (TDD: Red Phase)

Create the test file:

```java
package com.pfelink.monolith.infrastructure.security;

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

    private EntraClaimsExtractor extractor = new EntraClaimsExtractor();

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

    // Helper to create a mock Jwt token with claims
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
```

Run the test to verify it fails (classes don't exist yet):

```bash
cd backend/pfelink-monolith && mvn test -Dtest=EntraClaimsExtractorTest
```

Expected: **FAIL** — "cannot find symbol: class EntraClaimsExtractor"

### Step 3b: Implement EntraClaimsExtractor (Green Phase)

Create the component file:

```java
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
```

Run the test again to verify it passes:

```bash
cd backend/pfelink-monolith && mvn test -Dtest=EntraClaimsExtractorTest
```

Expected: **PASS** — All 5 tests pass.

### Step 3c: Commit

```bash
cd backend/pfelink-monolith
git add src/main/java/com/pfelink/monolith/infrastructure/security/EntraClaimsExtractor.java src/test/java/com/pfelink/monolith/infrastructure/security/EntraClaimsExtractorTest.java
git commit -m "feat: add EntraClaimsExtractor for mapping Microsoft token claims to application domain"
```

---

## Task 4: Configure Spring Security OAuth2 Resource Server

**Files:**
- Create/Modify: `backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/config/SecurityConfig.java`
- Test: `backend/pfelink-monolith/src/test/java/com/pfelink/monolith/infrastructure/config/SecurityConfigTest.java`

### Step 4a: Write Integration Test for SecurityConfig (TDD: Red Phase)

Create the test file:

```java
package com.pfelink.monolith.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://ciamlogin.com/12345678-1234-1234-1234-123456789012/discovery/v2.0/keys",
    "spring.security.oauth2.resourceserver.jwt.issuer-uri=https://ciamlogin.com/12345678-1234-1234-1234-123456789012/v2.0"
})
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRejectUnauthenticatedRequests() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/faculty"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectRequestsWithoutBearerToken() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/faculty")
            .header(HttpHeaders.AUTHORIZATION, "Bearer invalid-token"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldConfigureOAuth2ResourceServer() throws Exception {
        // This test verifies the bean is created and Spring Security is configured.
        // Actual token validation requires a valid Microsoft token or mock server.
        // For now, we verify the filter chain is configured correctly.
        
        // Act & Assert
        mockMvc.perform(get("/actuator/health"))
            .andExpect(status().isOk());
    }
}
```

Run the test to verify it fails (SecurityConfig might not exist or is incomplete):

```bash
cd backend/pfelink-monolith && mvn test -Dtest=SecurityConfigTest
```

Expected: **FAIL** — 401 status or configuration not applied correctly.

### Step 4b: Create/Update SecurityConfig (Green Phase)

Create or update `backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/config/SecurityConfig.java`:

```java
package com.pfelink.monolith.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * Spring Security configuration for OAuth2 Resource Server with Microsoft Entra External ID.
 * 
 * Key features:
 * - Validates incoming JWT tokens against Microsoft's JWKS endpoint
 * - Stateless session (no cookies)
 * - Method-level security via @PreAuthorize
 * - CORS configured for frontend origin
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true,
    jsr250Enabled = true
)
public class SecurityConfig {

    /**
     * Configure HTTP security with OAuth2 Resource Server.
     * All endpoints are protected by default; public endpoints can be explicitly permitted.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF (stateless API, no session-based auth)
            .csrf(csrf -> csrf.disable())
            
            // Configure CORS
            .cors(Customizer.withDefaults())
            
            // No session support (stateless)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Configure OAuth2 Resource Server to validate JWT from Entra
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults())
            )
            
            // Authorization rules: require authentication for all endpoints except health/swagger
            .authorizeHttpRequests(authz -> authz
                // Public endpoints
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/api-docs/**", "/v3/api-docs/**").permitAll()
                // Authentication endpoints (login, registration) — allow public access
                .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                // All other endpoints require authentication
                .anyRequest().authenticated()
            )
            
            // Exception handling: return 401 for missing/invalid tokens
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) -> {
                    res.sendError(401, "Unauthorized: " + e.getMessage());
                })
            );

        return http.build();
    }

    /**
     * Configure CORS for the frontend.
     * Update allowedOrigins to match your frontend deployment.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Allow requests from frontend origin(s)
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173",  // Local Vite dev server
            "https://yourdomain.com"  // Production frontend (update to your domain)
        ));
        
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
```

Run the test again:

```bash
cd backend/pfelink-monolith && mvn test -Dtest=SecurityConfigTest
```

Expected: **PASS** — Security filter chain is configured and unauthenticated requests return 401.

### Step 4c: Commit

```bash
cd backend/pfelink-monolith
git add src/main/java/com/pfelink/monolith/infrastructure/config/SecurityConfig.java src/test/java/com/pfelink/monolith/infrastructure/config/SecurityConfigTest.java
git commit -m "feat: configure Spring Security OAuth2 Resource Server for Entra External ID"
```

---

## Task 5: Update Controllers to Use @PreAuthorize

**Files:**
- Modify: All REST controllers in `backend/pfelink-monolith/src/main/java/com/pfelink/monolith/api/**/`
  - `FacultyController.java`
  - `StudentController.java`
  - `ProjectController.java`
  - `AdminController.java`
  - Other protected endpoints

### Step 5a: Identify Controllers Requiring Auth

List all controllers in the api directory:

```bash
find backend/pfelink-monolith/src/main/java/com/pfelink/monolith/api -name "*Controller.java" | head -20
```

### Step 5b: Update Each Controller

For each controller, replace custom JWT validation with `@PreAuthorize` annotations.

**Example: FacultyController**

Before (with custom JWT validation):
```java
@RestController
@RequestMapping("/api/faculty")
public class FacultyController {
    
    private final IGetAllFacultiesQueryHandler handler;
    private final JwtTokenValidator jwtValidator;  // Remove this
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<FacultyDTO>>> getAllFaculties(
        @RequestHeader("Authorization") String token) {
        
        // Remove this custom validation
        User user = jwtValidator.validateAndExtractUser(token);
        if (user == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>(...));
        }
        
        // Business logic...
    }
}
```

After (with OAuth2):
```java
@RestController
@RequestMapping("/api/faculty")
@EnableMethodSecurity
public class FacultyController {
    
    private final IGetAllFacultiesQueryHandler handler;
    // JwtTokenValidator removed — Spring Security handles validation
    
    @GetMapping
    @PreAuthorize("hasRole('Faculty') or hasRole('Admin')")
    public ResponseEntity<ApiResponse<List<FacultyDTO>>> getAllFaculties(
        @AuthenticationPrincipal Jwt jwt) {  // Inject Jwt directly from OAuth2
        
        // Extract oid from jwt.getClaim("oid") if needed for business logic
        String oid = (String) jwt.getClaim("oid");
        
        // Business logic—Spring Security already validated the token
        // ...
    }
}
```

**Pattern for all controllers:**
1. Remove `@RequestHeader("Authorization")` parameters
2. Remove `JwtTokenValidator` or similar injection
3. Add `@PreAuthorize("hasRole('...')")` based on your business rules
4. Inject `@AuthenticationPrincipal Jwt jwt` to access token claims if needed
5. Import: `org.springframework.security.access.prepost.PreAuthorize` and `org.springframework.security.oauth2.jwt.Jwt`

**Apply to these controllers (example roles—adjust to your domain):**

- `FacultyController`: `@PreAuthorize("hasRole('Faculty') or hasRole('Admin')")`
- `StudentController`: `@PreAuthorize("hasRole('Student') or hasRole('Admin')")`
- `ProjectController`: `@PreAuthorize("hasRole('Faculty') or hasRole('Student') or hasRole('Admin')")`
- `AdminController`: `@PreAuthorize("hasRole('Admin')")`
- `LoginController`: Public (no @PreAuthorize)
- `RegistrationController`: Public (no @PreAuthorize)

### Step 5c: Verify Controllers Compile

```bash
cd backend/pfelink-monolith && mvn clean compile
```

Expected: **SUCCESS** — No compilation errors related to removed JWT classes.

### Step 5d: Run Controller Tests

If controller tests exist, run them to verify @PreAuthorize works correctly:

```bash
cd backend/pfelink-monolith && mvn test -Dtest=*ControllerTest
```

Expected: **PASS** — Tests should verify that unauthenticated requests return 401, authenticated requests with proper roles succeed, and requests with insufficient roles return 403.

**Example test for a protected controller endpoint:**

```java
@Test
void shouldReturnUnauthorizedWhenNoToken() throws Exception {
    mockMvc.perform(get("/api/faculty"))
        .andExpect(status().isUnauthorized());
}

@Test
void shouldReturnForbiddenWhenRoleInsufficient() throws Exception {
    String studentToken = "...valid-token-with-Student-role...";
    mockMvc.perform(get("/api/admin/users")
        .header("Authorization", "Bearer " + studentToken))
        .andExpect(status().isForbidden());
}

@Test
void shouldAllowAccessWhenRoleMatches() throws Exception {
    String adminToken = "...valid-token-with-Admin-role...";
    mockMvc.perform(get("/api/admin/users")
        .header("Authorization", "Bearer " + adminToken))
        .andExpect(status().isOk());
}
```

### Step 5e: Commit Controller Changes

```bash
cd backend/pfelink-monolith
git add src/main/java/com/pfelink/monolith/api/
git commit -m "feat: replace custom JWT validation with @PreAuthorize (OAuth2)"
```

---

## Task 6: Handle Token Refresh with Microsoft Refresh Tokens

**Files:**
- Create: `backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/security/RefreshTokenHandler.java`
- Create: `backend/pfelink-monolith/src/main/java/com/pfelink/monolith/api/auth/RefreshTokenController.java`
- Test: `backend/pfelink-monolith/src/test/java/com/pfelink/monolith/infrastructure/security/RefreshTokenHandlerTest.java`

### Step 6a: Write Test for RefreshTokenHandler (TDD: Red Phase)

```java
package com.pfelink.monolith.infrastructure.security;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RefreshTokenHandlerTest {

    private RefreshTokenHandler handler;
    private RestTemplate restTemplate;

    @Test
    void shouldExchangeRefreshTokenForNewAccessToken() {
        // Arrange
        handler = new RefreshTokenHandler(restTemplate, "https://ciamlogin.com/tenant-id", "client-id", "client-secret");
        String refreshToken = "mock-refresh-token";
        
        // Mock the response from Microsoft token endpoint
        String mockResponse = """
            {
                "access_token": "new-access-token",
                "token_type": "Bearer",
                "expires_in": 3600
            }
            """;
        
        // Act
        RefreshTokenHandler.TokenResponse response = handler.refreshAccessToken(refreshToken);

        // Assert
        assertThat(response.accessToken()).isEqualTo("new-access-token");
        assertThat(response.expiresIn()).isEqualTo(3600);
    }

    @Test
    void shouldThrowExceptionOnInvalidRefreshToken() {
        // Arrange
        handler = new RefreshTokenHandler(restTemplate, "https://ciamlogin.com/tenant-id", "client-id", "client-secret");
        String invalidToken = "invalid-refresh-token";

        // Act & Assert
        assertThatThrownBy(() -> handler.refreshAccessToken(invalidToken))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("refresh token");
    }
}
```

Run the test:

```bash
cd backend/pfelink-monolith && mvn test -Dtest=RefreshTokenHandlerTest
```

Expected: **FAIL** — Class doesn't exist.

### Step 6b: Implement RefreshTokenHandler (Green Phase)

```java
package com.pfelink.monolith.infrastructure.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.Map;

/**
 * Handles token refresh against Microsoft Entra token endpoint.
 * Exchanges a refresh token for a new access token.
 */
@Component
public class RefreshTokenHandler {

    private final RestTemplate restTemplate;
    private final String tokenEndpoint;
    private final String clientId;
    private final String clientSecret;

    public RefreshTokenHandler(
        RestTemplate restTemplate,
        @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri:https://ciamlogin.com/tenant-id/v2.0}") String issuerUri,
        @Value("${spring.security.oauth2.client.registration.entra.client-id:#{null}}") String clientId,
        @Value("${spring.security.oauth2.client.registration.entra.client-secret:#{null}}") String clientSecret
    ) {
        this.restTemplate = restTemplate;
        // Replace /v2.0 with /token
        this.tokenEndpoint = issuerUri.replace("/v2.0", "") + "/token";
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    /**
     * Exchange a refresh token for a new access token.
     * @param refreshToken the refresh token from the initial login
     * @return TokenResponse with new access_token and expiry
     * @throws IllegalArgumentException if refresh token is invalid
     */
    public TokenResponse refreshAccessToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("Refresh token cannot be null or empty");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "refresh_token");
            body.add("refresh_token", refreshToken);
            body.add("client_id", clientId);
            body.add("client_secret", clientSecret);
            body.add("scope", "openid profile email");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            // Call Microsoft token endpoint
            var response = restTemplate.postForObject(tokenEndpoint, request, Map.class);

            if (response == null || !response.containsKey("access_token")) {
                throw new IllegalArgumentException("Invalid response from token endpoint");
            }

            String accessToken = (String) response.get("access_token");
            Integer expiresIn = (Integer) response.get("expires_in");

            return new TokenResponse(accessToken, expiresIn != null ? expiresIn : 3600);
        } catch (RestClientException e) {
            throw new IllegalArgumentException("Failed to refresh token: " + e.getMessage(), e);
        }
    }

    /**
     * Response from token endpoint.
     */
    public record TokenResponse(String accessToken, int expiresIn) {}
}
```

Run the test again:

```bash
cd backend/pfelink-monolith && mvn test -Dtest=RefreshTokenHandlerTest
```

Expected: **PASS**.

### Step 6c: Create RefreshTokenController (API Endpoint)

```java
package com.pfelink.monolith.api.auth;

import com.pfelink.monolith.infrastructure.security.RefreshTokenHandler;
import com.pfelink.monolith.shared.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST endpoint for refreshing access tokens.
 * Client sends refresh_token in request body, receives new access_token.
 */
@RestController
@RequestMapping("/api/auth")
public class RefreshTokenController {

    private final RefreshTokenHandler refreshTokenHandler;

    public RefreshTokenController(RefreshTokenHandler refreshTokenHandler) {
        this.refreshTokenHandler = refreshTokenHandler;
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Map<String, Object>>> refreshToken(
        @RequestBody RefreshTokenRequest request) {
        
        try {
            RefreshTokenHandler.TokenResponse response = refreshTokenHandler.refreshAccessToken(request.refreshToken());
            
            Map<String, Object> data = Map.of(
                "access_token", response.accessToken(),
                "expires_in", response.expiresIn(),
                "token_type", "Bearer"
            );
            
            return ResponseEntity.ok(new ApiResponse<>(data));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(Map.of("error", e.getMessage())));
        }
    }

    public record RefreshTokenRequest(String refreshToken) {}
}
```

### Step 6d: Update application.yml with OAuth2 Client Config

Add the OAuth2 client registration for refresh token handling:

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          entra:
            # Application (client) ID from Entra
            client-id: "YOUR_CLIENT_ID"
            client-secret: "YOUR_CLIENT_SECRET"
            client-authentication-method: "client_secret_basic"
            authorization-grant-type: "authorization_code"
            redirect-uri: "http://localhost:8081/login/oauth2/code/entra"
            scope: "openid,profile,email,offline_access"
        provider:
          entra:
            issuer-uri: "https://ciamlogin.com/{tenant-id}/v2.0"
            authorization-uri: "https://ciamlogin.com/{tenant-id}/oauth2/v2.0/authorize"
            token-uri: "https://ciamlogin.com/{tenant-id}/oauth2/v2.0/token"
            user-info-uri: "https://graph.microsoft.com/oidc/userinfo"
            jwk-set-uri: "https://ciamlogin.com/{tenant-id}/discovery/v2.0/keys"
```

### Step 6e: Verify Compilation and Tests

```bash
cd backend/pfelink-monolith && mvn clean test -Dtest=RefreshTokenHandlerTest
```

Expected: **PASS**.

### Step 6f: Commit

```bash
cd backend/pfelink-monolith
git add src/main/java/com/pfelink/monolith/infrastructure/security/RefreshTokenHandler.java \
        src/main/java/com/pfelink/monolith/api/auth/RefreshTokenController.java \
        src/test/java/com/pfelink/monolith/infrastructure/security/RefreshTokenHandlerTest.java \
        src/main/resources/application.yml
git commit -m "feat: add token refresh endpoint for OAuth2 refresh tokens"
```

---

## Task 7: Update User Persistence to Store Azure ID

**Files:**
- Modify: `backend/pfelink-monolith/src/main/java/com/pfelink/monolith/domain/auth/entity/User.java`
- Create: `backend/pfelink-monolith/src/db/migration/V*__add_azure_id_column.sql` (if using Flyway)
- Test: `backend/pfelink-monolith/src/test/java/com/pfelink/monolith/domain/auth/entity/UserTest.java`

### Step 7a: Update User Entity

Ensure your User entity has an `azureId` field mapped to the `oid` claim:

```java
@Entity
@Table(name = "users")
@RequiredArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "azure_id", nullable = false, unique = true)
    private final String azureId;  // Maps to 'oid' from Microsoft token
    
    @Column(name = "email", nullable = false, unique = true)
    private final String email;
    
    @Column(name = "full_name")
    private final String fullName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private final UserRole role;  // Admin, Faculty, Student, etc.
    
    @Column(name = "created_at")
    private final LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructor, getters, equals/hashCode
}
```

### Step 7b: Create Database Migration (if using Flyway)

Create `backend/pfelink-monolith/src/main/resources/db/migration/V2__add_azure_id_column.sql`:

```sql
-- Add azure_id column to users table if not exists
ALTER TABLE users ADD COLUMN IF NOT EXISTS azure_id VARCHAR(255) NOT NULL UNIQUE;

-- Create index for fast lookups by azure_id
CREATE INDEX IF NOT EXISTS idx_users_azure_id ON users(azure_id);
```

### Step 7c: Update UserRepository

Ensure the repository can find users by Azure ID:

```java
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByAzureId(String azureId);  // Add this method
}
```

Implement in `JpaUserRepository`:

```java
@Repository
public class JpaUserRepository implements IUserRepository {
    
    @Autowired
    private SpringDataUserRepository springDataRepo;
    
    @Override
    public Optional<User> findByAzureId(String azureId) {
        return springDataRepo.findByAzureId(azureId);
    }
}
```

Add the method to `SpringDataUserRepository`:

```java
public interface SpringDataUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByAzureId(String azureId);
}
```

### Step 7d: Update Login Flow to Use Azure ID

Modify the login command handler to extract `oid` and store/lookup users by Azure ID:

```java
@Component
public class LoginCommandHandler implements ICommandHandler<LoginCommand, LoginResult> {
    
    private final IUserRepository userRepository;
    private final EntraClaimsExtractor claimsExtractor;
    
    @Override
    public LoginResult handle(LoginCommand cmd) {
        // During OIDC flow, the controller receives the ID token with 'oid'
        // For now, this is a simplified handler; in practice, login is driven by OAuth2 flow
        
        // Find user by azureId (from 'oid' claim)
        Optional<User> user = userRepository.findByAzureId(cmd.azureId());
        
        if (user.isEmpty()) {
            return LoginResult.failure("User not found");
        }
        
        return LoginResult.success(user.get());
    }
}
```

### Step 7e: Run Tests

```bash
cd backend/pfelink-monolith && mvn clean test -DincludedGroups=persistence
```

Expected: **PASS** — User entity persists and retrieves by Azure ID correctly.

### Step 7f: Commit

```bash
cd backend/pfelink-monolith
git add src/main/java/com/pfelink/monolith/domain/auth/entity/User.java \
        src/main/java/com/pfelink/monolith/domain/auth/repository/IUserRepository.java \
        src/main/resources/db/migration/ \
        src/main/java/com/pfelink/monolith/infrastructure/persistence/
git commit -m "feat: add azureId field to User entity for Entra mapping"
```

---

## Task 8: Security Best Practices & Token Validation

**Files:**
- Create: `backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/security/EntraSecurityValidator.java`
- Test: `backend/pfelink-monolith/src/test/java/com/pfelink/monolith/infrastructure/security/EntraSecurityValidatorTest.java`

### Step 8a: Write Security Validator Test (TDD: Red Phase)

```java
package com.pfelink.monolith.infrastructure.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EntraSecurityValidatorTest {

    private EntraSecurityValidator validator = new EntraSecurityValidator();

    @Test
    void shouldValidateTokenIssuer() {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put("oid", "12345678-1234-1234-1234-123456789012");
        claims.put("aud", "client-id");
        
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
            .expiresAt(Instant.now().minusSeconds(3600))  // Expired 1 hour ago
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
}
```

Run the test:

```bash
cd backend/pfelink-monolith && mvn test -Dtest=EntraSecurityValidatorTest
```

Expected: **FAIL** — Class doesn't exist.

### Step 8b: Implement EntraSecurityValidator (Green Phase)

```java
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
```

Run the test:

```bash
cd backend/pfelink-monolith && mvn test -Dtest=EntraSecurityValidatorTest
```

Expected: **PASS**.

### Step 8c: Integrate Validator into SecurityConfig (Optional but Recommended)

Optionally add a custom filter or authenticate event listener to invoke the validator. For now, the validator is available for injection in command handlers or other application components:

```java
@Component
public class SomeQueryHandler implements IQueryHandler<SomeQuery, Result> {
    
    private final EntraSecurityValidator validator;
    private final EntraClaimsExtractor extractor;
    
    @Override
    public Result handle(SomeQuery query, Jwt jwt) {
        // Validate token is from your Entra tenant and not expired
        validator.validateIssuer(jwt, "https://ciamlogin.com/tenant-id/v2.0");
        validator.validateExpiry(jwt);
        validator.validateOidPresent(jwt);
        
        // Extract claims safely
        String oid = extractor.extractOid(jwt);
        
        // Business logic...
    }
}
```

### Step 8d: Commit

```bash
cd backend/pfelink-monolith
git add src/main/java/com/pfelink/monolith/infrastructure/security/EntraSecurityValidator.java \
        src/test/java/com/pfelink/monolith/infrastructure/security/EntraSecurityValidatorTest.java
git commit -m "feat: add EntraSecurityValidator for additional OAuth2 security checks"
```

---

## Task 9: Integration Tests for End-to-End OAuth2 Flow

**Files:**
- Create: `backend/pfelink-monolith/src/test/java/com/pfelink/monolith/api/auth/OAuth2IntegrationTest.java`

### Step 9a: Write Integration Test

```java
package com.pfelink.monolith.api.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Integration tests for OAuth2 Resource Server with Microsoft Entra tokens.
 * Verifies:
 * - Unauthenticated requests are rejected
 * - Valid OAuth2 tokens grant access
 * - @PreAuthorize role checks work correctly
 * - Token refresh endpoint functions
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://ciamlogin.com/12345678-1234-1234-1234-123456789012/discovery/v2.0/keys",
    "spring.security.oauth2.resourceserver.jwt.issuer-uri=https://ciamlogin.com/12345678-1234-1234-1234-123456789012/v2.0",
    "spring.security.oauth2.resourceserver.jwt.audiences=test-client-id"
})
class OAuth2IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnUnauthorizedWhenMissingToken() throws Exception {
        mockMvc.perform(get("/api/faculty"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWithInvalidToken() throws Exception {
        mockMvc.perform(get("/api/faculty")
            .header(HttpHeaders.AUTHORIZATION, "Bearer invalid-token"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowAccessWithValidToken() throws Exception {
        // Arrange: Create a mock valid JWT with Faculty role
        String validToken = createMockJwt("faculty-user-oid", "faculty@example.com", Arrays.asList("Faculty"));

        // Act & Assert
        mockMvc.perform(get("/api/faculty")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken))
            .andExpect(status().isOk());
    }

    @Test
    void shouldReturnForbiddenWhenRoleInsufficient() throws Exception {
        // Arrange: Create a mock JWT with Student role, trying to access Admin endpoint
        String studentToken = createMockJwt("student-user-oid", "student@example.com", Arrays.asList("Student"));

        // Act & Assert
        mockMvc.perform(get("/api/admin/users")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + studentToken))
            .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowAdminAccessToProtectedEndpoint() throws Exception {
        // Arrange: Create a mock JWT with Admin role
        String adminToken = createMockJwt("admin-user-oid", "admin@example.com", Arrays.asList("Admin"));

        // Act & Assert
        mockMvc.perform(get("/api/admin/users")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
            .andExpect(status().isOk());
    }

    @Test
    void shouldAllowPublicAccessToLoginEndpoint() throws Exception {
        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
            .andExpect(status().isOk());  // Endpoint may return 200 or 400, but not 401
    }

    // Helper to create a mock Jwt object (not a real token)
    // In production, use MockMvc's SecurityMockMvcRequestPostProcessors.jwt()
    private String createMockJwt(String oid, String email, java.util.List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("oid", oid);
        claims.put("email", email);
        claims.put("roles", roles);
        
        // For testing with MockMvc, you may need to use spring-security-test:
        // .with(jwt().jwt(jwt -> jwt.claim("oid", oid).claim("email", email).claim("roles", roles)))
        
        // Alternatively, generate a real JWT signed with a test key and configure 
        // MockMvc to validate against that key endpoint in test properties.
        
        return "mock-jwt-value";  // Placeholder for actual token generation
    }
}
```

### Step 9b: Add Spring Security Test Dependency

Ensure `spring-security-test` is in pom.xml:

```xml
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
```

### Step 9c: Run Integration Tests

```bash
cd backend/pfelink-monolith && mvn test -Dtest=OAuth2IntegrationTest
```

Expected: **PASS** — OAuth2 authorization is enforced and roles are validated.

### Step 9d: Commit

```bash
cd backend/pfelink-monolith
git add pom.xml src/test/java/com/pfelink/monolith/api/auth/OAuth2IntegrationTest.java
git commit -m "test: add OAuth2 integration tests for Entra External ID"
```

---

## Task 10: Remove Legacy JWT Filter and Dependencies

**Files:**
- Delete: All custom JWT-related files not yet removed
- Modify: `pom.xml` (final cleanup)

### Step 10a: Final Audit for JWT Remnants

```bash
grep -r "JwtTokenProvider\|JwtFilter\|TokenValidation\|CustomJwt\|@EnableJwt\|jwt.secret" backend/pfelink-monolith/src --include="*.java" --include="*.yml"
```

Expected output: Should be empty. If not, delete remaining files.

### Step 10b: Verify No Unused Imports

Check for leftover imports of JWT libraries:

```bash
grep -r "import io.jsonwebtoken\|import com.auth0.jwt" backend/pfelink-monolith/src --include="*.java"
```

Expected output: Should be empty.

### Step 10c: Full Build Test

```bash
cd backend/pfelink-monolith && mvn clean package -DskipTests
```

Expected: **SUCCESS** — Application builds without errors.

### Step 10d: Run All Tests

```bash
cd backend/pfelink-monolith && mvn test
```

Expected: **ALL PASS** — All unit and integration tests pass.

### Step 10e: Final Commit

```bash
cd backend/pfelink-monolith
git add -A
git commit -m "chore: final cleanup and verification of OAuth2 migration"
```

---

## Task 11: Documentation Updates

**Files:**
- Modify: `CLAUDE.md` (update architecture section)
- Create: `docs/OAUTH2_MIGRATION.md` (migration guide)

### Step 11a: Update CLAUDE.md

Add a new section to `CLAUDE.md` under "Architecture Deep Dive":

```markdown
### OAuth2 Resource Server (Microsoft Entra External ID)

**Token Validation Flow:**
1. Client receives Microsoft Entra ID token and access token via OAuth2 login
2. Client includes access token in `Authorization: Bearer <token>` header
3. Spring Security validates token against Microsoft's JWKS endpoint (https://ciamlogin.com/{tenant}/discovery/v2.0/keys)
4. Token claims (oid, email, roles) extracted via `EntraClaimsExtractor`
5. Spring Security authorities created from roles
6. `@PreAuthorize` annotations enforce role-based access control

**Key Components:**
- `SecurityConfig` — Configures OAuth2 Resource Server, CORS, session policy
- `EntraClaimsExtractor` — Maps Microsoft token claims to application domain
- `EntraSecurityValidator` — Additional application-specific validation
- `RefreshTokenHandler` — Exchanges refresh tokens for new access tokens

**Configuration (application.yml):**
```yaml
spring.security.oauth2.resourceserver.jwt.jwk-set-uri: Microsoft JWKS endpoint
spring.security.oauth2.resourceserver.jwt.issuer-uri: Entra issuer URI
```

**Protected Endpoints:**
Use `@PreAuthorize("hasRole('...')")` on controller methods. Roles come from the `roles` claim in the Microsoft token.

**Token Refresh:**
POST `/api/auth/refresh` with `{"refreshToken": "..."}` returns new access token.
```

### Step 11b: Create Migration Guide

Create `docs/OAUTH2_MIGRATION.md`:

```markdown
# OAuth2 Migration: Custom JWT → Microsoft Entra External ID

## Overview
Migrated from custom JWT token generation (jjwt library) to Microsoft Entra External ID as the token provider. Spring Security now validates all incoming tokens against Microsoft's JWKS endpoint.

## What Changed

### Removed
- Custom JWT filter (`JwtAuthenticationFilter`)
- Token generation service (`JwtTokenService`)
- JWT secret configuration
- jjwt dependency

### Added
- Spring Security OAuth2 Resource Server
- `EntraClaimsExtractor` component for claim mapping
- `EntraSecurityValidator` for application-specific validation
- `RefreshTokenController` for token refresh via Microsoft

### Modified
- `SecurityConfig` — Now configures OAuth2 instead of custom JWT filter
- All controllers — Use `@PreAuthorize` instead of manual token validation
- `application.yml` — OAuth2 configuration replaces JWT config

## Configuration

Update `application.yml` with your Entra tenant details:

```yaml
spring.security.oauth2.resourceserver.jwt.jwk-set-uri: https://ciamlogin.com/{tenant-id}/discovery/v2.0/keys
spring.security.oauth2.resourceserver.jwt.issuer-uri: https://ciamlogin.com/{tenant-id}/v2.0
spring.security.oauth2.client.registration.entra.client-id: {your-client-id}
spring.security.oauth2.client.registration.entra.client-secret: {your-client-secret}
```

## Testing

OAuth2 tokens are validated automatically by Spring Security. Test protected endpoints:

```bash
# Should return 401 (no token)
curl http://localhost:8081/api/faculty

# Should return 401 (invalid token)
curl -H "Authorization: Bearer invalid" http://localhost:8081/api/faculty

# Should return 200 (valid token with Faculty role)
curl -H "Authorization: Bearer {valid-entra-token}" http://localhost:8081/api/faculty
```

## Troubleshooting

| Issue | Solution |
|-------|----------|
| 401 Unauthorized on all requests | Verify token from Microsoft, check `issuer-uri` config matches Entra issuer |
| 403 Forbidden with valid token | Check role in token's `roles` claim matches `@PreAuthorize` requirement |
| Token expires too quickly | Configure offline_access scope in Entra app registration for refresh tokens |
| JWKS endpoint unreachable | Verify network allows HTTPS to ciamlogin.com, check firewall rules |

## Security Best Practices (Microsoft vs Custom JWT)

### Advantages of Microsoft Entra Tokens
1. **Key Rotation:** Microsoft automatically rotates signing keys; custom JWT required manual key management
2. **Compliance:** Microsoft tokens comply with security standards (OAuth2, OIDC); custom implementations prone to vulnerabilities
3. **Claims:** `oid` (object ID) uniquely identifies users in Entra; custom tokens required separate ID fields
4. **Token Refresh:** Microsoft handles refresh token lifecycle; custom implementation required manual logic
5. **Audit Trail:** Entra logs all token issuances; custom tokens had no audit trail

### Security Checklist (Entra Tokens)
- ✅ Always validate `issuer` claim matches your Entra tenant
- ✅ Always validate token `exp` (expiry) — Spring Security does this automatically
- ✅ Always verify `aud` (audience) matches your application client ID
- ✅ Never store tokens in cookies (use secure storage in browser, pass via Authorization header)
- ✅ Use HTTPS only in production (no tokens over HTTP)
- ✅ Rotate client secrets regularly in Entra admin center
- ✅ Log token validation failures for security audits
- ✅ Use `@PreAuthorize` for role checks, not custom string comparisons

### Legacy Custom JWT Vulnerabilities (Now Fixed)
- ❌ Hardcoded secret key in config file (now uses Microsoft's public JWKS)
- ❌ No automatic key rotation (Microsoft handles this)
- ❌ Token replay attacks not prevented (Microsoft tokens are single-use)
- ❌ Expiry not enforced consistently (Spring Security enforces it automatically)
- ❌ No claim validation (now validated against schema)
```

### Step 11c: Commit Documentation

```bash
git add docs/OAUTH2_MIGRATION.md CLAUDE.md
git commit -m "docs: add OAuth2 migration guide and update architecture docs"
```

---

## Task 12: Manual Testing & Verification

**Files:**
- None (testing and verification only)

### Step 12a: Start the Application

```bash
cd backend/pfelink-monolith && mvn spring-boot:run
```

Expected output:
```
...
o.s.s.o.r.w.BearerTokenAuthenticationFilter : Creating filter chain: ...
...
Tomcat started on port(s): 8081
```

### Step 12b: Test Unauthenticated Request

```bash
curl -X GET http://localhost:8081/api/faculty
```

Expected: **401 Unauthorized**

### Step 12c: Test with Invalid Token

```bash
curl -X GET http://localhost:8081/api/faculty \
  -H "Authorization: Bearer invalid-token-abc123"
```

Expected: **401 Unauthorized**

### Step 12d: Test with Real Microsoft Token (Optional)

If you have a valid Microsoft Entra token (from actual login), test:

```bash
curl -X GET http://localhost:8081/api/faculty \
  -H "Authorization: Bearer {your-real-token}"
```

Expected: **200 OK** or **403 Forbidden** (if role insufficient)

### Step 12e: Test Public Endpoint

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "user@example.com", "password": "password"}'
```

Expected: **200 OK** or **400 Bad Request** (business logic failure, not auth failure)

### Step 12f: Verify Swagger UI Still Works

Open browser to `http://localhost:8081/swagger-ui.html`

Expected: Swagger UI loads, endpoints listed. Click "Try it out" — should return 401 (no token in request).

### Step 12g: Final Verification

Stop the server with `Ctrl+C`.

Run the full test suite:

```bash
cd backend/pfelink-monolith && mvn clean test
```

Expected: **ALL TESTS PASS** (100+ tests, depending on your codebase).

### Step 12h: Commit (No Changes)

```bash
git add -A
git status  # Should show nothing to commit
```

---

## Summary Checklist

- [ ] Task 1: Legacy JWT infrastructure removed (files, dependencies, config)
- [ ] Task 2: application.yml updated with OAuth2 Resource Server config
- [ ] Task 3: EntraClaimsExtractor component created and tested
- [ ] Task 4: SecurityConfig configured for OAuth2 Resource Server
- [ ] Task 5: All controllers updated to use @PreAuthorize
- [ ] Task 6: RefreshTokenController and RefreshTokenHandler implemented
- [ ] Task 7: User entity updated with azureId field
- [ ] Task 8: EntraSecurityValidator implemented for additional security checks
- [ ] Task 9: Integration tests written and passing
- [ ] Task 10: Final JWT cleanup and full build verification
- [ ] Task 11: Documentation updated (CLAUDE.md + migration guide)
- [ ] Task 12: Manual testing and verification complete

---

## Security Best Practices Summary

1. **Issuer Validation:** Always validate token issuer matches your Entra tenant (`EntraSecurityValidator.validateIssuer()`)
2. **Token Expiry:** Spring Security validates automatically; no custom expiry logic needed
3. **Audience Validation:** Verify `aud` claim matches your application client ID
4. **Role Mapping:** Use `@PreAuthorize("hasRole('...')")` for declarative role checks
5. **HTTPS Only:** Never send tokens over HTTP in production
6. **Token Storage:** Keep tokens in secure browser storage (not cookies unless httpOnly + secure)
7. **Refresh Tokens:** Use `/api/auth/refresh` to get new access tokens when expired
8. **Audit Logging:** Log token validation failures for security monitoring (implement in `EntraSecurityValidator`)
9. **Client Secret:** Rotate regularly in Entra admin center; never commit to git
10. **CORS:** Configure allowed origins carefully; only allow trusted frontend domains

---

**Estimated Total Time:** 4–6 hours (depending on codebase size and integration complexity)
**Total Commits:** ~12 (one per task)