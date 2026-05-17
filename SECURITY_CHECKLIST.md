# Security Hardening Checklist - JWT Authentication & Rate Limiting

**Last Updated:** 2026-05-17  
**Status:** ✅ Fully Implemented

---

## Authentication & Token Security

### JWT Configuration
- ✅ Mandatory JWT secret via `JWT_SECRET` environment variable (no hardcoded defaults)
- ✅ Base64-encoded 256-bit key requirement documented
- ✅ Access token expiration: 15 minutes (configurable via `JWT_ACCESS_TOKEN_EXPIRATION`)
- ✅ Refresh token expiration: 7 days (configurable via `JWT_REFRESH_TOKEN_EXPIRATION`)
- ✅ JTI (JWT ID) claim for token revocation tracking
- ✅ Token validation with signature verification using HS256 algorithm

### Token Blacklist & Revocation
- ✅ Redis-backed token blacklist for logout
- ✅ Automatic TTL cleanup matching token expiration
- ✅ Prevents replay attacks of invalidated tokens
- ✅ Efficient O(1) lookup using Redis

### Token Rotation
- ✅ Refresh tokens rotated on every use
- ✅ Old refresh tokens invalidated immediately
- ✅ Prevents token reuse across sessions
- ✅ Implemented in LoginCommandHandler and RefreshTokenCommandHandler

---

## Input Validation

### Request DTOs
- ✅ LoginRequest
  - Email: @Email, @Size(max=255), @NotBlank
  - Password: @Size(min=8, max=128), @NotBlank
  
- ✅ RefreshTokenRequest
  - Token: @Size(min=100, max=2000), @NotBlank

### Validation Scope
- ✅ Validation at HTTP boundary (controllers only)
- ✅ Jakarta Validation Framework (standard)
- ✅ Size constraints prevent buffer overflow attacks
- ✅ Type validation prevents injection attacks

---

## Rate Limiting

### Login Endpoint Protection
- ✅ Maximum 5 login attempts per 15 minutes per IP address
- ✅ Returns HTTP 429 (Too Many Requests) when limit exceeded
- ✅ Rate limit key includes client IP (with X-Forwarded-For support)
- ✅ Applied to: `/api/auth/login`, `/api/auth/register`, `/api/auth/refresh`

### Implementation
- ✅ RateLimitingInterceptor (Spring interceptor)
- ✅ Redis-backed counter with automatic TTL
- ✅ WebConfig registers interceptor globally

---

## Security Headers

### HTTP Security Headers (enabled)
- ✅ HSTS (Strict-Transport-Security)
  - `max-age: 31536000` (1 year)
  - `includeSubDomains: true`
  - `preload: true`
  
- ✅ CSP (Content-Security-Policy)
  - `default-src: 'self'`
  - `script-src: 'self' 'unsafe-inline'` (consider removing unsafe-inline in production)
  - `style-src: 'self' 'unsafe-inline'` (consider removing unsafe-inline in production)
  
- ✅ X-Frame-Options: DENY (clickjacking protection)
- ✅ X-Content-Type-Options: nosniff (MIME sniffing prevention)
- ✅ X-XSS-Protection: 1; mode=block
- ✅ Referrer-Policy: strict-origin-when-cross-origin
- ✅ Permissions-Policy: camera=(), microphone=(), geolocation=()

### Cookie Security
- ✅ httpOnly flag (no JavaScript access)
- ✅ Secure flag (HTTPS only, dynamic based on server.ssl.enabled)
- ✅ SameSite=Strict (CSRF protection)
- ✅ Access token cookie: 15 minutes
- ✅ Refresh token cookie: 7 days

---

## Audit & Monitoring

### Authentication Logging
- ✅ Failed login attempts logged at WARN level with email
- ✅ Unverified email login attempts logged
- ✅ Successful logins logged at INFO level with email
- ✅ JWT filter errors logged at DEBUG level

### Recommended Monitoring
```bash
# Failed login attempts
tail -f app.log | grep "Failed login attempt"

# Unverified email attempts
tail -f app.log | grep "unverified email"

# Rate limit violations
tail -f app.log | grep "Rate limit exceeded"
```

---

## CORS & API Security

### CORS Configuration (from SecurityConfig)
```yaml
allowed-origins:
  - http://localhost:3000
  - http://localhost:5173
  - http://localhost:8080
  - http://localhost:8081
methods:
  - GET, POST, PUT, DELETE, PATCH
credentials: true
```

### Recommendations
- ⚠️ Update allowed origins for production (remove localhost)
- ⚠️ Use environment variables for CORS origins
- ✅ Credentials allowed (needed for cookie-based auth)

---

## HTTPS & Transport Security

### HTTP/2 Support
- ✅ HTTP/2 enabled for better performance
- ✅ Multiplexing reduces connection overhead

### SSL/TLS Configuration (for production)
```yaml
server:
  ssl:
    enabled: true
    key-store: ${SSL_KEYSTORE_PATH}
    key-store-password: ${SSL_KEYSTORE_PASSWORD}
    key-store-type: PKCS12
    key-alias: tomcat
```

### Certificate Setup
```bash
# Generate self-signed certificate for development
keytool -genkey -alias tomcat -storetype PKCS12 \
  -keyalg RSA -keysize 2048 -keystore keystore.p12 \
  -validity 365 -storepass password

# Or use Let's Encrypt in production
# See: https://letsencrypt.org/
```

---

## Password Security

### Password Hashing
- ✅ BCryptPasswordEncoder with 12 salt rounds (configured in SecurityConfig)
- ✅ Never stored as plaintext
- ✅ Salted and peppered automatically

### Password Validation
```
LoginRequest password: @Size(min=8, max=128)
```

---

## Database Security

### Prepared Statements
- ✅ JPA with parameterized queries (prevents SQL injection)
- ✅ Hibernate automatically parameterizes all queries
- ✅ Never concatenate user input into SQL

### Connection Pooling
- ✅ HikariCP configured
- ✅ Max connections: 30
- ✅ Min idle: 10
- ✅ Connection timeout: 10 seconds

---

## Dependency Vulnerability Scanning

### OWASP Dependency Check
- ✅ Maven plugin configured (v9.0.8)
- ✅ Fails build on CVE severity >= 7.0
- ✅ Generates reports: HTML, JSON, XML

### Run Vulnerability Scan
```bash
cd backend/pfelink-monolith

# Run dependency check
mvn org.owasp:dependency-check-maven:check

# Reports generated in: target/dependency-check-report.*
```

### Current Dependencies
- spring-boot-starter-web (3.4.2)
- spring-boot-starter-security (3.4.2)
- spring-boot-starter-data-jpa (3.4.2)
- postgresql (latest)
- jjwt (0.12.3) - ✅ Current/secure
- cloudinary-http44 (1.36.0)
- spring-boot-starter-data-redis (3.4.2)

---

## Secrets Management

### Required Environment Variables
```bash
# JWT Configuration (REQUIRED)
JWT_SECRET=<base64-encoded-32-byte-key>
JWT_ACCESS_TOKEN_EXPIRATION=900000
JWT_REFRESH_TOKEN_EXPIRATION=604800000

# Database (REQUIRED)
DB_HOST=localhost
DB_PORT=5433
DB_USERNAME=postgres
DB_PASSWORD=<password>

# Redis (OPTIONAL, defaults to localhost:6380)
SPRING_DATA_REDIS_HOST=localhost
SPRING_DATA_REDIS_PORT=6380

# Mail (OPTIONAL)
EMAIL_USERNAME=<email>
EMAIL_PASSWORD=<password>

# Cloudinary (OPTIONAL)
CLOUDINARY_CLOUD_NAME=<cloud_name>
CLOUDINARY_API_KEY=<api_key>
CLOUDINARY_API_SECRET=<api_secret>

# Frontend (OPTIONAL)
FRONTEND_URL=http://localhost:5173
RECAPTCHA_ENABLED=false

# Admin (OPTIONAL)
ADMIN_EMAIL=admin@test.com
ADMIN_PASSWORD=<password>

# SSL/TLS (OPTIONAL, for HTTPS)
SSL_KEYSTORE_PATH=/path/to/keystore.p12
SSL_KEYSTORE_PASSWORD=<keystore_password>
SERVER_SSL_ENABLED=true
```

### Never Commit
```
❌ .env
❌ .env.local
❌ *.pem
❌ *.key
❌ keystore.p12
```

### .gitignore Check
```bash
cat .gitignore | grep -E "\.env|\.pem|\.key|keystore"
```

---

## OWASP Top 10 Coverage

| Vulnerability | Status | Implementation |
|---|---|---|
| 1. Injection | ✅ Mitigated | JPA parameterized queries, input validation |
| 2. Broken Authentication | ✅ Mitigated | JWT signature validation, token blacklist, rate limiting |
| 3. Broken Access Control | ✅ Mitigated | @PreAuthorize on endpoints, role-based checks |
| 4. Insecure Deserialization | ✅ Mitigated | Spring Security handles serialization |
| 5. Broken Cryptography | ✅ Mitigated | HS256, BCrypt hashing, HTTPS enforced |
| 6. XXE | ✅ Mitigated | Spring disables XXE by default |
| 7. Insufficient Logging | ✅ Mitigated | Structured logging on auth events |
| 8. CSRF | ✅ Mitigated | SameSite=Strict cookies, CSRF tokens |
| 9. Using Known Vulnerable Components | ✅ Monitored | Dependency-Check Maven plugin |
| 10. Insufficient Rate Limiting | ✅ Mitigated | RateLimitingInterceptor on auth endpoints |

---

## Pre-Production Checklist

### Before Deploying to Production

- [ ] Generate strong JWT secret: `openssl rand -base64 32`
- [ ] Configure HTTPS with valid SSL certificate
- [ ] Set `SERVER_SSL_ENABLED=true` in environment
- [ ] Update CORS allowed-origins to production domains only
- [ ] Remove `'unsafe-inline'` from CSP if possible
- [ ] Run `mvn org.owasp:dependency-check-maven:check` and review results
- [ ] Enable Prometheus metrics at `/actuator/metrics`
- [ ] Configure log aggregation (ELK, CloudWatch, etc.)
- [ ] Set up alerts for failed login attempts
- [ ] Set up alerts for rate limit violations
- [ ] Review admin credentials - change from default
- [ ] Verify recaptcha is enabled and configured
- [ ] Test token refresh flow
- [ ] Test logout and token blacklist
- [ ] Load test rate limiting behavior
- [ ] Run security headers check: https://securityheaders.com
- [ ] Enable database SSL connections
- [ ] Configure Redis with authentication in production
- [ ] Set up database backups with encryption
- [ ] Document incident response procedures

---

## Testing Security

### Unit Tests
```bash
mvn test -Dtest=LoginCommandHandlerTest
mvn test -Dtest=JwtServiceTest
```

### Integration Tests
```bash
mvn test -DincludedGroups=integration
```

### Manual Testing
```bash
# Test failed login
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"wrong"}'

# Test rate limiting
for i in {1..10}; do
  curl -X POST http://localhost:8081/api/auth/login \
    -H "Content-Type: application/json" \
    -d '{"email":"test@example.com","password":"password"}'
done

# Test token validation
curl -H "Authorization: Bearer <token>" \
  http://localhost:8081/api/v1/students/me
```

---

## References

- [OWASP Top 10 2023](https://owasp.org/www-project-top-ten/)
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [JWT Best Practices](https://tools.ietf.org/html/rfc7519)
- [NIST Password Guidelines](https://pages.nist.gov/800-63-3/sp800-63b.html)
- [OWASP Dependency Check](https://owasp.org/www-project-dependency-check/)

---

**Maintained by:** Security Team  
**Last Reviewed:** 2026-05-17  
**Next Review:** 2026-08-17 (quarterly)
