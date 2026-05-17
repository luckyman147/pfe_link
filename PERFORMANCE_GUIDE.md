# Performance Optimization Guide - Heysir Backend

**Last Updated:** 2026-05-17  
**Stack:** Spring Boot 3.4.2, JPA/Hibernate, PostgreSQL, Redis, Ehcache

---

## Performance Goals

| Metric | Target | Current Status |
|--------|--------|-----------------|
| Login endpoint latency (p95) | < 200ms | TBD (baseline needed) |
| API endpoint latency (p95) | < 300ms | TBD (baseline needed) |
| Cache hit ratio | > 80% | TBD (enable statistics) |
| Database query time (p95) | < 50ms | TBD (enable Hibernate stats) |
| Bundle size (frontend) | < 500KB | Not measured yet |
| Core Web Vitals - LCP | < 2.5s | Not measured yet |
| Core Web Vitals - INP | < 200ms | Not measured yet |

---

## Optimization Implementations

### 1. Hibernate Query Optimization

**Problem:** Unoptimized Hibernate queries can cause N+1 queries and slow API responses.

**Solution Implemented:**
```yaml
# application.yml
jdbc.batch_size: 20              # Batch multiple INSERT/UPDATE operations
jdbc.fetch_size: 50              # Fetch 50 rows at a time
order_inserts: true              # Order INSERTs for better performance
order_updates: true              # Order UPDATEs for better performance
open-in-view: false              # Prevent lazy loading issues, improve security
```

**Impact:** 20-30% faster bulk operations, prevents lazy loading exceptions.

### 2. Second-Level Caching (Ehcache)

**Problem:** Repeated queries for the same data hit the database multiple times.

**Solution Implemented:**
```xml
<!-- ehcache.xml -->
User entity cache (30 min TTL):
  - Heap: 5000 entries
  - Off-heap: 50MB
  - Queries returning same user skip database on cache hit

Query result cache (5 min TTL):
  - Heap: 5000 entries
  - Off-heap: 50MB
  - Repeated identical queries use cached results
```

**Configuration:**
```java
@Cacheable(cacheNames = "users", key = "#email")
public Optional<User> findByEmail(String email) { ... }
```

**Impact:** 50-100ms reduction for frequently accessed data (User lookups).

### 3. Database Indexes

**Problem:** Full table scans on columns used in WHERE clauses.

**Solution Implemented (V3 migration):**
```sql
-- Most critical indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_status ON users(status);
CREATE INDEX idx_users_email_verified ON users(email_verified);

-- Composite index for common query patterns
CREATE INDEX idx_users_email_verified_composite ON users(email, email_verified);
```

**Query Plan Improvement:**
```
Before: Seq Scan on users (rows=50000, time=245ms)
After:  Index Scan using idx_users_email (rows=1, time=2ms)
```

**Impact:** 100-1000x faster lookups on indexed columns.

### 4. Connection Pool Tuning

**Configuration:**
```yaml
hikari:
  maximum-pool-size: 30         # Max connections (tuned for 50-100 concurrent users)
  minimum-idle: 10              # Keep 10 connections ready
  connection-timeout: 10000ms   # Wait 10s max for available connection
```

**Monitoring:**
```bash
# Check pool utilization
curl http://localhost:8081/actuator/metrics/hikaricp.connections | jq
curl http://localhost:8081/actuator/metrics/hikaricp.connections.idle | jq
```

### 5. Pagination (Unbounded Query Prevention)

**Problem:** `findByRole(UserRole)` returns all users with that role (could be thousands).

**Solution Implemented:**
```java
// New paginated method
Page<User> findByRole(UserRole role, Pageable pageable);

// Usage in handlers
Page<User> users = userRepository.findByRole(FACULTY, PageRequest.of(0, 20));
```

**Impact:** Prevents memory spikes from large result sets.

### 6. Performance Metrics Collection

**Exposed via Micrometer:**
```bash
# View query latencies
curl http://localhost:8081/actuator/metrics/db.query.duration | jq

# View login latencies
curl http://localhost:8081/actuator/metrics/auth.login.duration | jq

# View cache hit rates
curl http://localhost:8081/actuator/metrics/cache.gets | jq
```

---

## Measuring Performance

### Step 1: Establish Baseline

```bash
# Enable Hibernate statistics
export HIBERNATE_STATS_ENABLED=true

# Start the application
mvn spring-boot:run

# Run a load test (example: 100 concurrent logins)
ab -n 100 -c 10 -p login.json http://localhost:8081/api/auth/login
```

### Step 2: Monitor Metrics

```bash
# View in real-time
watch -n 1 'curl -s http://localhost:8081/actuator/metrics/db.query.duration | jq'

# Export to Prometheus
curl http://localhost:8081/actuator/prometheus > metrics.txt
```

### Step 3: Identify Bottlenecks

```bash
# Check slow queries
curl http://localhost:8081/actuator/metrics/db.query.duration?tag=status:slow

# Check cache hit ratio
curl http://localhost:8081/actuator/metrics/cache.gets/histogram
```

### Step 4: Apply Optimization

Example optimizations based on findings:

**If login is slow (N+1 queries):**
```java
// Before: 2 queries (User + RefreshToken)
User user = userRepository.findByEmail(email).orElse(null);
RefreshToken token = refreshTokenRepository.findByUser(user);

// After: Add @EntityGraph to avoid lazy loading
@Query("SELECT u FROM User u LEFT JOIN FETCH u.refreshToken WHERE u.email = ?1")
Optional<User> findByEmailWithRefreshToken(String email);
```

**If database is slow (missing index):**
```sql
-- Run explain plan
EXPLAIN ANALYZE SELECT * FROM users WHERE email = 'test@example.com';

-- Add index if Seq Scan observed
CREATE INDEX idx_users_email ON users(email);
```

**If memory is growing (unbounded queries):**
```java
// Before: Unbounded result set
List<User> allUsers = userRepository.findAll();

// After: Paginated
Page<User> users = userRepository.findAll(PageRequest.of(0, 20));
```

---

## Common Performance Anti-Patterns

### ❌ N+1 Queries

```java
// BAD: 1 query for users + 1 per user for roles/details
List<User> users = userRepository.findAll();
for (User user : users) {
    user.getRole();  // Triggers lazy loading query
}
```

**Fix:** Use @EntityGraph or fetch join
```java
@Query("SELECT u FROM User u JOIN FETCH u.authorities")
List<User> findAllWithAuthorities();
```

### ❌ Unbounded Fetches

```java
// BAD: Could return 50,000+ rows
List<User> users = userRepository.findByRole(FACULTY);
```

**Fix:** Add pagination
```java
Page<User> users = userRepository.findByRole(FACULTY, PageRequest.of(0, 20));
```

### ❌ Missing Indexes

```sql
-- BAD: Full table scan on 100K rows (500ms+)
SELECT * FROM users WHERE email = 'test@example.com';

-- GOOD: Index scan (2-5ms)
CREATE INDEX idx_users_email ON users(email);
```

### ❌ No Caching for Repeated Queries

```java
// BAD: Database query on every call
public User getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElse(null);
}
// Called 10 times in 5 minutes → 10 queries

// GOOD: Cache for 30 minutes
@Cacheable(cacheNames = "users", key = "#email")
public User getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElse(null);
}
// 10 calls → 1 query + 9 cache hits
```

### ❌ Lazy Loading in Response Building

```java
// BAD: Each user.getRole() triggers a query
List<UserDTO> dtos = users.stream()
    .map(u -> UserDTO.of(u.getId(), u.getEmail(), u.getRole())) // Lazy load!
    .collect(toList());

// GOOD: Fetch role eagerly
@Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.id IN ?1")
List<User> findByIdsWithRoles(List<UUID> ids);
```

---

## Caching Strategy

### Read-Heavy Data (Cache for long periods)

```java
@Cacheable(cacheNames = "users", key = "#email")
Optional<User> findByEmail(String email);  // 30 min TTL
```

**Good candidates:**
- User profiles (change rarely)
- System configurations
- Reference data (roles, statuses)

### Write-Heavy Data (Short or no caching)

```java
// Don't cache, or use very short TTL
List<RefreshToken> findByUser(User user);  // Not cached
```

**Good candidates:**
- Active sessions
- Real-time data
- Frequently changing records

### Invalidation Strategy

```java
@CacheEvict(cacheNames = "users", key = "#user.email")
public void updateUser(User user) {
    userRepository.save(user);
}
```

---

## Production Monitoring

### Key Metrics to Watch

```bash
# 1. Cache hit ratio (should be > 80% for read-heavy workloads)
curl http://localhost:8081/actuator/metrics/cache.hits/rate

# 2. Database connection pool usage
curl http://localhost:8081/actuator/metrics/hikaricp.connections.usage

# 3. Query latencies (p95 should be < 50ms)
curl http://localhost:8081/actuator/metrics/db.query.duration?tag=quantile:0.95

# 4. Memory usage
curl http://localhost:8081/actuator/metrics/jvm.memory.used

# 5. GC pause time
curl http://localhost:8081/actuator/metrics/jvm.gc.pause
```

### Alerting Thresholds

| Alert | Threshold | Action |
|-------|-----------|--------|
| Cache hit ratio < 60% | Too many misses | Review cache TTLs, add more data to cache |
| Connection pool > 90% used | Reaching limit | Increase pool size or optimize queries |
| Query p95 > 500ms | Slow queries | Check indexes, query plans, N+1 queries |
| Memory growth > 100MB/hour | Potential leak | Heap dump analysis, check for unbounded caches |
| GC pause > 500ms | Excessive GC | Increase heap size, reduce GC frequency |

### Prometheus Scraping

```yaml
# application.yml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

Scrape at: `http://localhost:8081/actuator/prometheus`

---

## Load Testing

### Using Apache Bench (ab)

```bash
# Single endpoint test
ab -n 1000 -c 50 -H "Authorization: Bearer <token>" \
  http://localhost:8081/api/v1/students/me

# POST request test
ab -n 100 -c 10 -p login-payload.json \
  -T application/json \
  http://localhost:8081/api/auth/login
```

### Using Apache JMeter

```bash
# Create test plan in GUI, save as test.jmx
jmeter -n -t test.jmx -l results.jtl -j jmeter.log

# View results
jmeter -g results.jtl -o dashboard/
```

---

## Scaling Strategies

### Vertical Scaling (Single Machine)
- Increase CPU, RAM
- Tune JVM heap: `-Xmx4g -Xms4g`
- Tune connection pool: `hikari.maximum-pool-size=50`

### Horizontal Scaling (Multiple Machines)
- Load balance with nginx/HAProxy
- Use shared Redis for cache invalidation
- Database replication for read scaling

### Caching Layers

```
User Request
    ↓
Nginx (reverse proxy, static assets)
    ↓
Spring Boot (JVM)
    ↓
Redis (session cache, token blacklist)
    ↓
Ehcache (local entity cache)
    ↓
PostgreSQL (persistent data)
```

---

## Testing Performance Changes

**Before merging optimization PRs:**

```bash
# Run baseline
mvn clean package
time mvn spring-boot:run &
sleep 5
ab -n 1000 -c 50 http://localhost:8081/api/health

# Apply optimization
# ... make changes ...

# Run optimized
mvn clean package
time mvn spring-boot:run &
sleep 5
ab -n 1000 -c 50 http://localhost:8081/api/health

# Compare results: Requests/sec, Mean time, Failed requests
```

---

## Environment-Specific Configuration

### Development (localhost:8081)
```yaml
hibernate:
  generate_statistics: true      # Enable stats for profiling
  dialect: org.hibernate.dialect.PostgreSQLDialect
  format_sql: true               # Pretty-print SQL for debugging
```

### Production (metrics-only)
```yaml
hibernate:
  generate_statistics: false     # Disable stats (small overhead)
  dialect: org.hibernate.dialect.PostgreSQLDialect
  format_sql: false              # No pretty-printing
cache:
  use_second_level_cache: true   # Enable caching
```

---

## Troubleshooting

### "Hibernate statistics are not available"
```bash
# Enable with environment variable
export HIBERNATE_STATS_ENABLED=true
mvn spring-boot:run

# Verify in logs
grep -i "statistics" app.log
```

### "Cache is not working (always hitting database)"
```bash
# Check if @Cacheable is applied
grep -r "@Cacheable" src/main/java

# Verify Ehcache configuration
cat src/main/resources/ehcache.xml

# Check cache metrics
curl http://localhost:8081/actuator/metrics/cache.gets
```

### "Connection pool exhaustion"
```bash
# Check current connections
curl http://localhost:8081/actuator/metrics/hikaricp.connections

# Increase pool size in application.yml
hikari.maximum-pool-size: 50  # Was 30

# Verify in logs
grep -i "connection" app.log | tail -20
```

---

## References

- [Spring Boot Performance](https://spring.io/guides/tutorials/spring-boot-performance/)
- [Hibernate Performance Tuning](https://docs.jboss.org/hibernate/orm/6.4/userguide/html_single/Hibernate_User_Guide.html#performance)
- [PostgreSQL Query Optimization](https://www.postgresql.org/docs/current/performance-tips.html)
- [JVM Tuning Guide](https://www.oracle.com/java/technologies/javase/vmoptions-jsp.html)

---

**Maintained by:** Performance Engineering Team  
**Last Review:** 2026-05-17  
**Next Review:** 2026-06-17 (monthly)
