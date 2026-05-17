# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Heysir** is a full-stack application with:
- **Backend:** Spring Boot 3.4.2 monolith (Java 17) using CQRS + DDD patterns
- **Frontend:** React 18 + TypeScript + Vite SPA
- **Primary Database:** PostgreSQL (HikariCP pooling)
- **Cache/Queue:** Redis
- **File Storage:** Cloudinary
- **Auth:** OAuth2 Resource Server with Microsoft Entra External ID

**Architecture Style:** Hexagonal (Ports & Adapters) with CQRS + Domain-Driven Design layers:
- `api/` → REST Controllers (request → Response)
- `application/` → Commands/Queries & Handlers (orchestration)
- `domain/` → Entities, ValueObjects, Repositories (business rules)
- `infrastructure/` → Persistence, Events, Configs, Email
- `shared/` → CQRS interfaces, utilities, Result<T>

## Backend Build & Test Commands

### Maven Builds (run from `backend/pfelink-monolith/`)

```bash
# Compile & package
mvn clean package

# Compile only (no tests)
mvn clean compile

# Run tests
mvn test

# Run single test class
mvn test -Dtest=YourTestClassName

# Run single test method
mvn test -Dtest=YourTestClassName#testMethodName

# Run integration tests only
mvn test -DincludedGroups=integration

# Run application locally
mvn spring-boot:run

# Skip tests during build
mvn clean package -DskipTests
```

### IDE Integration
- IntelliJ IDEA: Open `backend/pfelink-monolith` as project root
- VS Code: Use Maven extension or VS Code Maven plugin
- HotSwap: DevTools included for live reload during development

## Frontend Build & Test Commands

### Node Scripts (run from `frontend/`)

```bash
# Install dependencies
npm install

# Dev server (HMR enabled on http://localhost:5173)
npm run dev

# Production build
npm run build

# Preview production build locally
npm run preview

# Lint with ESLint
npm run lint

# Format with Prettier (if configured)
npm run format

# Type check only (no build)
npx tsc --noEmit
```

## Architecture Deep Dive

### CQRS Pattern (Command Query Responsibility Segregation)

**Command Flow** (Write Operations):
```
Controller → Dispatcher.send(Command) → CommandHandler.handle() → Domain → Repositories
```

**Query Flow** (Read Operations):
```
Controller → Dispatcher.query(Query) → QueryHandler.handle() → Repositories → DTO
```

All commands/queries implement their respective marker interfaces in `shared.cqrs`:
- `ICommand<R>` — write operation returning type R
- `ICommandHandler<C, R>` — handles command C, returns R
- `IQuery<R>` — read operation returning type R  
- `IQueryHandler<Q, R>` — handles query Q, returns R

### Layer Responsibilities

#### API Layer (`api/`)
- REST `@RestController` classes only
- Map HTTP requests → Commands/Queries
- Map Results → HTTP responses (ApiResponse wrapper)
- **NO business logic**
- Use `RequestTimingFilter` for request metrics (in infrastructure.api)

**Key Classes:**
- `ApiResponse<T>` — standardized response envelope
- `ResponseUtil` — helper for response building
- `RequestTimingFilter` — logs request duration

#### Application Layer (`application/`)
- Commands under `command/` (organized by domain: auth, academic, etc.)
- Queries under `query/` (same organization)
- Each command/query in its own package with Command/Query/CommandHandler/QueryHandler
- **Minimal orchestration only** — delegate business logic to domain
- DTOs (Request/Response objects) live here
- Event publishing here (after domain operations succeed)

**Key Pattern:**
```java
// Command lives in: application/auth/command/login/
public record LoginCommand(String email, String password) implements ICommand<LoginResult> {}

// Handler lives in: application/auth/command/login/
@Component
public class LoginCommandHandler implements ICommandHandler<LoginCommand, LoginResult> {
    public LoginResult handle(LoginCommand cmd) {
        // 1. Validate (minimal)
        // 2. Call domain logic
        // 3. Publish events
        // 4. Return result
    }
}
```

#### Domain Layer (`domain/`)
- **Core business logic** — no Spring dependencies (except @Entity for JPA)
- Entities, Value Objects, Aggregates
- Repository **interfaces** only (`domain/*/repository/I*Repository.java`)
- Enums and constraints here
- No DTOs or responses — return domain objects

**Key Subdirectories:**
- `entity/` — JPA entities and aggregates
- `enums/` — business enums (UserRole, AccountStatus, etc.)
- `repository/` — repository interfaces (I-prefix convention)

#### Infrastructure Layer (`infrastructure/`)
- **Spring/Framework-specific implementations**
- Repository implementations (`persistence/`)
- Email sending (`email/`)
- Event listeners (`event/listeners/`)
- Spring configs (`config/`)
- File uploads via Cloudinary (`storage/`)
- Filters and interceptors (`api/`)

**Persistence Pattern:**
```
domain/auth/repository/IUserRepository.java (interface)
  ↓
infrastructure/persistence/auth/SpringDataUserRepository.java (extends JpaRepository)
  ↓
infrastructure/persistence/auth/JpaUserRepository.java (implements IUserRepository)
```

#### Shared Layer (`shared/`)
- CQRS marker interfaces (ICommand, IQuery, ICommandHandler, IQueryHandler)
- `Dispatcher` interface (Spring finds @Component implementations)
- `Result<T>` wrapper for success/failure returns
- Utilities (constants, helpers)

### Event-Driven Async Processing

Spring `@EventListener` pattern for decoupled operations:

1. **Events** live in `infrastructure/event/events/` (domain-organized: auth, faculty, selection, etc.)
2. **Listeners** in `infrastructure/event/listeners/` 
3. Handlers trigger `ApplicationEventPublisher.publishEvent(DomainEvent)` after command succeeds
4. Listeners react asynchronously (email, notifications, state updates)

**Example:** User signs up → `UserSignedUpEvent` published → `AuthNotificationListener` emails verification link

**Configuration:** `@EnableAsync` in `AsyncConfig` with thread pool settings.

### Database & Persistence

**PostgreSQL Connection:**
- Host/port/credentials via environment variables in `application.yml`
- Defaults: localhost:5433, user: postgres, password: postgres
- HikariCP pooling: 10 max connections, 5 minimum idle
- JPA `ddl-auto: update` (auto-creates/updates schema on startup)
- Hibernate dialect: `PostgreSQLDialect`

**Redis Caching:**
- Host/port via env vars (defaults: localhost:6380)
- Timeout: 2000ms
- Currently unconfigured for `@Cacheable` — add as needed

## OAuth2 Resource Server (Microsoft Entra External ID)

**Token Validation Flow:**
1. Client receives Microsoft Entra ID token via OIDC login to `ciamlogin.com`
2. Client includes access token in `Authorization: Bearer <token>` header
3. Spring Security validates token against Microsoft's JWKS endpoint
4. Token claims (oid, email, roles) extracted via `EntraClaimsExtractor`
5. Spring Security authorities created from roles
6. `@PreAuthorize` annotations enforce role-based access control

**Configuration (application.yml):**
```yaml
spring.security.oauth2.resourceserver.jwt:
  jwk-set-uri: "https://ciamlogin.com/{tenant-id}/discovery/v2.0/keys"
  issuer-uri: "https://ciamlogin.com/{tenant-id}/v2.0"
  audiences: "your-app-client-id"
```

**Key Components:**
- `SecurityConfig` — OAuth2 Resource Server setup, CORS, stateless session policy
- `EntraClaimsExtractor` — Maps Microsoft token claims (oid, email, roles) to application domain
- `EntraSecurityValidator` — Additional validation (issuer, audience, expiry, oid checks)
- `RefreshTokenHandler` — Exchanges refresh tokens for new access tokens via Microsoft endpoint
- `@PreAuthorize("hasRole('ROLE_NAME')")` — Declarative role-based access control on endpoints

**User Entity:**
- `azureId` field stores the 'oid' (object ID) from Microsoft Entra, uniquely identifying users
- Repository methods: `findByEmail()`, `findByAzureId()`
- Tokens validated against Microsoft's key set; no local secret management needed

**Protected Endpoints Example:**
```java
@RestController
@RequestMapping("/api/faculty")
public class FacultyController {
    @GetMapping
    @PreAuthorize("isAuthenticated()")  // Any authenticated user
    public ResponseEntity<?> getAllFaculties() { ... }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")   // Admin only
    public ResponseEntity<?> createFaculty(...) { ... }
}
```

**Token Refresh:**
- POST `/api/auth/refresh-oauth2` with `{"refreshToken": "..."}` returns new access token
- Handles Microsoft's OAuth2 refresh token flow automatically

**Security Best Practices (Entra vs Legacy JWT):**
- ✅ Key rotation handled by Microsoft (no local key management)
- ✅ Tokens validated against live JWKS endpoint
- ✅ Issuer and audience validation prevents token misuse
- ✅ `oid` claim uniquely identifies users in Entra, immutable
- ✅ Stateless validation: no session storage needed
- ✅ HTTPS-only in production; tokens never over HTTP
- ✅ CORS configured for frontend origin(s) only
- ✅ Role-based access enforced via `@PreAuthorize` (not string comparison)

## Code Quality & Constraints (from .cursor/rules/)

### SOLID Principles (Mandatory)
- **S**ingle Responsibility: Max 100 lines per file
- **O**pen/Closed: Use interfaces for behavior, never `if/else` on types
- **L**iskov Substitution: Subclasses must not break parent contracts
- **I**nterface Segregation: No "Fat" services; split into smaller interfaces
- **D**ependency Inversion: Constructor inject dependencies, never `new` services

### YAGNI Principle (You Aren't Gonna Need It)
- **No speculative features** — only implement what's needed for current task
- **No "future-proof" abstractions** — add abstraction when 3rd similar case appears, not at 2
- **No utility interfaces for single implementations** — if one class implements it, inline the interface
- **No configurable knobs** unless config is actually used by multiple code paths
- Example: Don't create `IEmailProvider` interface unless you'll swap providers; start with concrete `EmailService`

### Low Design / Minimal Abstraction
- **Straight line of code is better than clever design.** Don't abstract until you need to.
- **Three-rule:** Extract/abstract only when you see the *same pattern 3+ times*, not at 2
- **No "layers upon layers"** — repository → service → handler is enough; don't add more
- **Composition over inheritance** — prefer field injection + delegation over class hierarchies
- Example anti-pattern: Creating `BaseHandler<T>` to avoid duplication in 2 handlers (wait for 3)
- Example good pattern: Duplicate code in 2 places → extract at 3, accept early duplication

### TDD Workflow (Mandatory)
1. Write failing JUnit 5 test first
2. Implement minimal code to pass
3. Refactor (keep tests green)
4. All tests must pass before committing

### DTO & Entity Rules
- **DTOs:** Use Java `record` (immutable, no `@Data`)
- **Manual mapping** or MapStruct (if cross-project mapping needed)
- **Entities:** `@Entity` with `@RequiredArgsConstructor` for final fields
- **Optional:** Always use `Optional<T>` for nullable returns, never null

### Constructor Injection (Preferred)
```java
@Component
public class UserService {
    private final IUserRepository repo;  // final required
    private final EmailService email;
    
    public UserService(IUserRepository repo, EmailService email) {
        this.repo = repo;
        this.email = email;
    }
}
```

### Security Checklist
- OAuth2 tokens (from Microsoft Entra) in Authorization header only
- Passwords hashed via `BCryptPasswordEncoder` for local storage
- `@PreAuthorize` SpEL used sparingly, no complex logic
- Stateless auth (no sessions in Spring Security)
- Token validation against Microsoft's JWKS endpoint (no local secret key)
- CORS configured in `application.yml` (dev origins only)
- Issuer, audience, expiry, and oid claims validated before processing

### File & Code Organization
- Max 100 lines per file — break into smaller classes/methods
- One public class per file
- Logic must not leak into Repositories or Controllers
- Package by feature, not layer (e.g., `application/auth/command/...`, not `application/commands/auth/...`)

**When to split a file:**
- If > 100 lines: Extract helper class or service
- If > 1 method with 20+ lines: Split into separate class
- If multiple concerns in one class: One responsibility per class
- Example: `UserService` (30 lines) + `UserValidator` (25 lines) + `UserMapper` (20 lines) instead of one 75-line file

### File Splitting Checklist
Before growing a file past 100 lines, ask:
- [ ] Does this class do more than one thing? → Extract new class
- [ ] Do methods share less than 50% of fields? → Split into separate classes
- [ ] Is the constructor parameter list > 5? → Too many dependencies, split responsibilities
- [ ] Are there private helper methods > 20 lines? → Extract to separate service
- [ ] Do you have `if/else` chains checking types? → Create interface + implementations instead
- [ ] Does the test class need > 200 lines? → Original class is too complex, split it

### Anti-Patterns (Never Do These)
- ❌ Large if/else chains checking object types (use polymorphism instead)
- ❌ God services with 10+ dependencies (split into focused services)
- ❌ Utility classes with static methods (inject real services via DI)
- ❌ Creating interfaces for single implementations (wait for the 2nd user)
- ❌ Duplicate error handling logic (extract to shared utility only after 3rd occurrence)
- ❌ Controllers calling repositories directly (must go through command/query handlers)
- ❌ Domain entities with Spring annotations beyond @Entity (keep Spring out of domain)

## Configuration & Secrets

### application.yml Structure
- `spring.datasource.*` — PostgreSQL connection
- `spring.data.redis.*` — Redis config
- `spring.jpa.*` — Hibernate settings
- `spring.mail.*` — SMTP (Gmail in dev, use env vars in prod)
- `cloudinary.*` — File storage API keys
- `app.*` — Custom settings (frontend URL, admin defaults, CORS origins)
- `logging.level.*` — Log levels by package
- `management.endpoints.*` — Actuator metrics exposure

### Environment Variables (Production)
```
DB_HOST, DB_PORT, DB_USERNAME, DB_PASSWORD
SPRING_DATA_REDIS_HOST, SPRING_DATA_REDIS_PORT
EMAIL_USERNAME, EMAIL_PASSWORD
CLOUDINARY_CLOUD_NAME, CLOUDINARY_API_KEY, CLOUDINARY_API_SECRET
FRONTEND_URL, ADMIN_PASSWORD, RECAPTCHA_ENABLED
```

**⚠️ Never commit secrets.** Use `.env` locally, inject via CI/CD in production.

## API Documentation

Swagger/OpenAPI enabled at:
```
http://localhost:8081/swagger-ui.html
http://localhost:8081/api-docs (JSON)
```

Configured in `OpenApiConfig` (infrastructure.config). Tags and operations auto-sorted alphabetically.

## Common Development Workflows

### Adding a New Feature (Auth Example)

1. **Define domain first** (domain/auth/entity/, domain/auth/enums/)
   - Create `User` entity, `UserRole` enum, business rules
2. **Create repository interface** (domain/auth/repository/IUserRepository)
   - Define query methods (signatures only)
3. **Implement repository** (infrastructure/persistence/auth/)
   - Create `SpringDataUserRepository` (extends JpaRepository<User, Long>)
   - Create `JpaUserRepository` (implements IUserRepository, wraps Spring Data)
4. **Create command/query** (application/auth/command/)
   - `RegisterCommand`, `RegisterCommandHandler`
   - Handler calls domain, publishes event
5. **Create DTOs** (application/auth/dto/)
   - `RegisterRequest` (record), `RegisterResponse` (record)
6. **Create controller** (api/auth/)
   - Maps HTTP → Command/Query → Response
   - No business logic
7. **Create event & listener** (infrastructure/event/)
   - `UserRegisteredEvent`, `AuthNotificationListener`
   - Listen and send welcome email
8. **Write tests** (JUnit 5, mocks)
   - Test command handler, repository, event listener
9. **Run:** `mvn clean package && mvn spring-boot:run`

### Debugging

- **Logs:** `application.yml` sets `com.pfelink.monolith` to DEBUG
- **Dev mode:** `mvn spring-boot:run` includes hot reload (DevTools)
- **Profiler:** Actuator metrics at `/actuator/metrics` (enabled in config)
- **Database:** Connect to PostgreSQL on localhost:5433 (check `application.yml` defaults)

### Testing Strategy

- **Unit Tests:** Mock repositories, test handlers/services in isolation
- **Integration Tests:** Use `@DataJpaTest` for persistence, real DB in CI
- **Naming:** `*Test.java` (unit), `*IntegrationTest.java` (integration)
- **Coverage:** Aim for >80% on business logic; 100% on domain entities

```java
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository repo;
    
    @Test
    void shouldFindByEmail() {
        User user = new User("test@example.com", ...);
        repo.save(user);
        Optional<User> found = repo.findByEmail("test@example.com");
        assertThat(found).isPresent();
    }
}
```

## Frontend Structure (React + Vite)

- **Entry:** `src/main.tsx`
- **App routes:** `src/App.tsx` (React Router v6)
- **Components:** `src/components/` (organized by feature/domain)
- **Pages:** `src/pages/`
- **API client:** `src/services/api.ts` (axios or fetch wrapper)
- **Type definitions:** `src/types/` (TypeScript interfaces)
- **Styling:** CSS Modules or Tailwind (check vite.config.ts)

### Frontend-Backend Contract
- **DTOs match:** Frontend `types/` should mirror backend `application/*/dto/`
- **API endpoints:** Documented in `/swagger-ui.html` (test against live backend)
- **CORS:** Configured in `application.yml` (frontend origins listed)

## Performance Considerations

### Backend Optimization Points
1. **N+1 Queries:** Use `@EntityGraph` or fetch joins in repository queries
2. **Pagination:** Implement in query handlers for large result sets
3. **Caching:** Redis configured; use `@Cacheable` on read-heavy queries
4. **Connection Pooling:** HikariCP tuned (10 max, 5 min-idle); adjust if load increases
5. **Async Events:** Heavy ops (email, file processing) in `@EventListener` with `@Async`

### Frontend Optimization
- **Code Splitting:** Vite lazy-loads routes by default
- **Bundle Size:** Monitor with `npm run build` and check dist/ folder
- **DevTools:** React DevTools browser extension for profiling

## Cursor Rules Integration

This repo includes two `.cursor/rules/` files that enforce code quality:
1. **code.mdc:** Spring Boot expert role — caveman syntax, no yapping, focus on performance/security
2. **patterns-quality.mdc:** SOLID, YAGNI, DRY, max 100 lines/file, TDD workflow

These rules auto-apply to all Claude sessions in this repo. When implementing features, follow the TDD workflow and keep methods small (<30 lines is ideal).

## Gstack (Web Browsing & Automation)

For all web browsing, use the `/browse` skill from gstack. Never use `mcp__claude-in-chrome__*` tools.

**Available gstack skills:**
- `/office-hours` — Schedule office hours
- `/plan-ceo-review` — Plan CEO review
- `/plan-eng-review` — Plan engineering review
- `/plan-design-review` — Plan design review
- `/design-consultation` — Design consultation
- `/design-shotgun` — Design shotgun
- `/design-html` — Design HTML
- `/review` — Review code or designs
- `/ship` — Ship changes
- `/land-and-deploy` — Land and deploy
- `/canary` — Canary deployment
- `/benchmark` — Benchmark performance
- `/browse` — Fast headless browser for navigation and interaction
- `/connect-chrome` — Connect to Chrome
- `/qa` — QA testing
- `/qa-only` — QA testing only
- `/design-review` — Design review
- `/setup-browser-cookies` — Setup browser cookies
- `/setup-deploy` — Setup deployment
- `/setup-gbrain` — Setup gBrain
- `/retro` — Retrospective
- `/investigate` — Investigate issues
- `/document-release` — Document release
- `/codex` — Codex tool
- `/cso` — CSO tool
- `/autoplan` — Auto-plan
- `/plan-devex-review` — Plan DevEx review
- `/devex-review` — DevEx review
- `/careful` — Careful mode
- `/freeze` — Freeze changes
- `/guard` — Guard mode
- `/unfreeze` — Unfreeze changes
- `/gstack-upgrade` — Upgrade gstack
- `/learn` — Learning mode

## Troubleshooting

| Issue | Solution |
|-------|----------|
| `mvn` command not found | Install Maven or use IntelliJ's bundled Maven |
| PostgreSQL connection fails | Verify `DB_HOST`, `DB_PORT`, credentials in `application.yml`; ensure Postgres is running |
| Redis connection times out | Check Redis is running on configured port (default 6380) |
| HotSwap not working | Ensure DevTools in pom.xml and IDE configured for hot reload |
| Tests fail with "No bean found" | Check `@SpringBootTest` imports all necessary `@Configuration` classes |
| Swagger UI blank | Verify `springdoc-openapi-starter-webmvc-ui` dependency in pom.xml; check logs for scan errors |

---

**Last Updated:** May 2026 | **Maintainers:** Use this guide to stay productive across Spring Boot 3.4+ codebases with CQRS/DDD patterns.
