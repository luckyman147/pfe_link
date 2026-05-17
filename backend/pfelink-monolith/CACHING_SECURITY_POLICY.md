# Caching Security Policy

**Effective Date:** 2026-05-17  
**Status:** Active

---

## Overview

This policy defines what data can be cached safely and what must never be cached to prevent security vulnerabilities.

---

## NEVER Cache (Tier 1 - Sensitive Data)

These fields must NEVER be cached in any form:

### Authentication & Credentials
- ❌ Password hashes (`User.password`)
- ❌ Refresh tokens (`RefreshToken.token`)
- ❌ Email verification tokens (`User.emailVerificationToken`)
- ❌ Password reset tokens
- ❌ JWT tokens (keep in Redis only, with TTL)
- ❌ Session identifiers
- ❌ API keys or secrets

### Personal Identifiable Information (PII)
- ❌ Full names (unless public profile)
- ❌ Phone numbers
- ❌ Email addresses (in some contexts)
- ❌ IP addresses
- ❌ Timestamps of sensitive actions (login times, etc.)

### State That Changes Frequently
- ❌ `User.emailVerified` status (changes on verification)
- ❌ `User.status` (Account status changes frequently)
- ❌ `RefreshToken` entries (invalidated on use)
- ❌ Any transactional data (orders, payments, etc.)

---

## SAFE to Cache (Tier 2 - With Conditions)

These can be cached WITH PROPER INVALIDATION:

### Entity Collections (Non-Sensitive)
- ✅ User roles (if separated into a Roles entity, NOT embedded in User)
- ✅ System configuration (rarely changes)
- ✅ Reference data (countries, statuses, enums)

### Query Results
- ✅ `SELECT id, email FROM users WHERE role = FACULTY` (5 min TTL)
- ✅ Read-only queries with no sensitive fields
- ✅ Aggregated data (counts, statistics)

### Caching Rules by Entity

| Entity | Cache? | TTL | Notes |
|--------|--------|-----|-------|
| User (full) | ❌ NO | - | Contains password, tokens |
| Role | ✅ YES | 1 hour | Immutable reference data |
| RefreshToken | ❌ NO | - | Single-use, revocable |
| Faculty | ✅ YES | 1 hour | Read-heavy, rarely changes |
| Season | ✅ YES | 1 day | Read-only reference |

---

## Cache Invalidation Strategy

### Automatic Invalidation

```java
// When User data changes, cache must be invalidated
@CacheEvict(cacheNames = "query_results", key = "'users_by_email'")
public User updateUser(User user) {
    return userRepository.save(user);
}

// When password changes, invalidate immediately
@CacheEvict(cacheNames = "auth_cache", allEntries = true)
public void changePassword(UUID userId, String newPassword) {
    // ... implementation
}
```

### Manual Invalidation

```java
@Autowired
private CacheManager cacheManager;

// Clear specific cache on critical updates
cacheManager.getCache("auth_cache").clear();
cacheManager.getCache("user_sessions").clear();
```

---

## Caching Best Practices

### ✅ DO

- Use shorter TTLs for frequently-changing data (5 minutes)
- Use longer TTLs for immutable reference data (1+ days)
- Cache query results instead of entities (fine-grained control)
- Implement cache invalidation on writes
- Monitor cache hit ratios
- Log cache evictions on sensitive operations
- Use separate caches for different sensitivity levels

### ❌ DON'T

- Cache entire entities that contain sensitive fields
- Use long TTLs (1+ hour) for entities that change
- Cache without invalidation strategy
- Mix sensitive and non-sensitive data in same cache
- Trust stale cached data for security decisions
- Cache based on email/username (privacy risk)
- Cache session data in application cache (use Redis only)

---

## Current Implementation

### Ehcache Configuration (ehcache.xml)

```xml
<!-- User entity cache: DISABLED (contains sensitive data) -->
<!-- Reason: password, tokens, verification status -->

<!-- Query result cache: 5 min TTL (safe, immutable queries) -->
<cache alias="org.hibernate.cache.internal.StandardQueryCache">
  <ttl unit="minutes">5</ttl>
</cache>

<!-- Collection cache: 1 hour TTL (non-sensitive collections) -->
<cache alias="com.pfelink.monolith.domain.auth.entity.User.authorities">
  <ttl unit="hours">1</ttl>
</cache>
```

### Hibernate Configuration (application.yml)

```yaml
# Cache disabled for entities with sensitive data
# Query result caching enabled (safe for read-only queries)
cache.use_second_level_cache: true
cache.use_query_cache: true
```

---

## Security Audit Checklist

Before caching new data, verify:

- [ ] Data classification: Is this sensitive (Tier 1) or safe (Tier 2)?
- [ ] If sensitive: Is cache disabled and documented?
- [ ] If safe: Is TTL appropriate for change frequency?
- [ ] Is cache invalidation implemented for writes?
- [ ] Are cache metrics monitored for stale data?
- [ ] Is this data PII or subject to compliance (GDPR, etc.)?
- [ ] Would stale cache data cause security/permission issues?
- [ ] Is cache accessible only to the application (not users)?

---

## Monitoring & Alerts

### Key Metrics

```bash
# Cache hit ratio (should be > 80% for safe data)
curl http://localhost:8081/actuator/metrics/cache.hits

# Stale cache detection (monitor for excessive evictions)
curl http://localhost:8081/actuator/metrics/cache.evictions

# Cache size (prevent unbounded growth)
curl http://localhost:8081/actuator/metrics/cache.size
```

### Alert Thresholds

| Alert | Threshold | Action |
|-------|-----------|--------|
| Cache hit ratio < 60% | Review TTLs |
| Cache evictions spike | Possible invalidation issue |
| Cache size > 90% of limit | Increase heap or reduce TTL |
| Stale data served | Invalidation not working |

---

## Compliance Notes

### GDPR Implications

- Right to be forgotten: Cached PII must be cleared on deletion request
- Data accuracy: Stale data violates accuracy requirement
- Data minimization: Don't cache more than needed

### Solution

- Use short TTLs for PII (5 minutes max)
- Implement automatic invalidation on data changes
- Log cache evictions for compliance audits
- Never cache account deletion data

---

## Migration Guide

If you need to cache new sensitive data:

1. **Don't cache the entity** - Create a DTO without sensitive fields
2. **Cache only safe queries** - Use query result cache instead
3. **Add invalidation** - Implement @CacheEvict on all write operations
4. **Test TTL** - Ensure stale data doesn't cause issues
5. **Monitor** - Check metrics for cache effectiveness
6. **Document** - Add to CACHING_SECURITY_POLICY.md

---

## References

- [Spring Cache Abstraction](https://spring.io/guides/gs/caching/)
- [Hibernate Second-Level Caching](https://docs.jboss.org/hibernate/orm/6.0/userguide/html_single/Hibernate_User_Guide.html#caching)
- [GDPR and Caching](https://gdpr.eu/article-17-right-to-be-forgotten/)
- [OWASP: Sensitive Data Exposure](https://owasp.org/www-project-top-ten/)

---

**Maintained by:** Security & Performance Team  
**Last Review:** 2026-05-17  
**Next Review:** 2026-06-17 (quarterly)
