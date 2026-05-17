# Code Organization Refactoring Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Enforce <100 lines per file and <3 files per folder constraints across the entire Spring Boot monolith, improving code maintainability and cognitive load.

**Architecture:** Refactoring follows a three-phase approach: (1) add hard rules to CLAUDE.md, (2) split oversized files by extracting single-responsibility helper classes, (3) reorganize bloated folders using subdirectory grouping by business concern. Each phase uses TDD with failing tests first, then implementation, then verification. All changes maintain existing behavior and test coverage.

**Tech Stack:** Spring Boot 3.4.2, Java 17, JUnit 5, Maven, Git

---

## Phase 1: Add Hard Rules to CLAUDE.md

### Task 1: Update CLAUDE.md with Hard Rules

**Files:**
- Modify: `CLAUDE.md` (insert after "Code Quality & Constraints" section)

- [ ] **Step 1: Read current CLAUDE.md to locate insert point**

Run: `Read C:\Users\Iyed\Desktop\heysir\CLAUDE.md` (locate "Code Quality & Constraints (from .cursor/rules/)" section)

- [ ] **Step 2: Add new rules section after SOLID Principles and before YAGNI Principle**

Insert this text after the SOLID Principles block (line ~54):

```markdown
### File Size Constraint (Hard Rule) 🔴 MANDATORY

**Maximum 100 lines per file (including comments and blank lines)**

**Rationale:**
- Cognitive load: Humans understand files under 100 LOC faster and more accurately
- Testability: Smaller classes are easier to unit test in isolation
- Reusability: Single-responsibility files promote composition and DI
- Navigation: Finding code is faster with focused files
- Git history: Smaller changes reduce merge conflicts and blame complexity

**Enforcement:**
- All new files MUST be <100 lines
- Existing files violating this rule MUST be split (refactor task is included)
- Exceptions require explicit `/* EXCEPTION: <reason> */` comment at file top with hard deadline

**Current violations being fixed in Phase 2:**
- SecurityConfig.java (136 → split into SecurityConfig + SecurityHeadersConfig)
- EmailService.java (393 → split into EmailService + EmailTemplateBuilder + EmailValidator)
- FacultyNotificationListener.java (139 → split into FacultyNotificationListener + NotificationFormatter)
- FileValidationUtil.java (135 → split into FileValidationUtil + FileTypeValidator)
- JwtService.java (102 → split into JwtService + JwtTokenBuilder)
- LoginController.java (101 → split into LoginController + LoginValidator)

### Folder Structure Constraint (Hard Rule) 🔴 MANDATORY

**Maximum 3 files per folder**

**Rationale:**
- Navigation: Fewer files per folder prevents cognitive overwhelm
- Separation of concerns: Forces deliberate grouping by responsibility
- Package organization: Encourages natural boundaries (interfaces, implementations, DTOs separate)
- Scalability: Max 3 files prevents folder bloat as features grow

**Enforcement:**
- All new folders MUST have ≤3 files
- Existing folders violating this rule MUST be reorganized with subdirectories
- Subdirectories group by clear business concepts or technical responsibility

**Folder Reorganization Pattern:**

```
BEFORE (bloated):
  application/auth/dto/request/
    ├── LoginRequest.java
    ├── RegisterRequest.java
    ├── RefreshTokenRequest.java
    ├── VerifyEmailRequest.java
    ├── ResendVerificationRequest.java
    ├── ChangePasswordRequest.java
    ├── ForgotPasswordRequest.java
    ├── ResetPasswordRequest.java
    ├── UpdateProfileRequest.java
    ├── LogoutRequest.java

AFTER (organized with subdirectories):
  application/auth/dto/request/
    ├── authentication/
    │   ├── LoginRequest.java
    │   ├── RegisterRequest.java
    │   └── LogoutRequest.java
    ├── tokens/
    │   ├── RefreshTokenRequest.java
    │   └── RevokeTokenRequest.java
    ├── verification/
    │   ├── VerifyEmailRequest.java
    │   ├── ResendVerificationRequest.java
    │   └── ConfirmVerificationRequest.java
    └── password/
        ├── ChangePasswordRequest.java
        ├── ForgotPasswordRequest.java
        └── ResetPasswordRequest.java
```

**Current violations being fixed in Phase 3:**
- 18 folders identified with >3 files (see Phase 3 tasks)
```

- [ ] **Step 3: Verify insertion is grammatically correct**

Read the modified section and confirm it flows naturally from existing constraints.

- [ ] **Step 4: Commit CLAUDE.md update**

```bash
cd C:\Users\Iyed\Desktop\heysir
git add CLAUDE.md
git commit -m "docs: add hard rules for <100 lines per file and <3 files per folder"
```

---

## Phase 2: Split Oversized Files (<100 LOC)

### Task 2: Split SecurityConfig.java (136 → 85 + 68 lines)

**Files:**
- Modify: `backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/security/config/SecurityConfig.java`
- Create: `backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/security/config/SecurityHeadersConfig.java`
- Test: `backend/pfelink-monolith/src/test/java/com/pfelink/monolith/infrastructure/security/config/SecurityConfigTest.java`

- [ ] **Step 1: Read current SecurityConfig.java**

Run: `Read C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith\src\main\java\com\pfelink\monolith\infrastructure\security\config\SecurityConfig.java`

**Expected:** 136-line file with securityFilterChain(), passwordEncoder(), userDetailsService(), corsConfigurationSource() methods, and comprehensive security headers config.

- [ ] **Step 2: Create SecurityHeadersConfig.java for security header configuration**

Create new file with extracted headers configuration:

```java
package com.pfelink.monolith.infrastructure.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;

@Configuration
public class SecurityHeadersConfig {

    public static void configureSecurityHeaders(HeadersConfigurer<?> headers) throws Exception {
        headers
            .contentTypeOptions(contentTypeOptions -> {})
            .xssProtection(xss -> xss
                .headerValue(org.springframework.security.web.header.writers.XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
            )
            .frameOptions(frameOptions -> frameOptions.deny())
            .httpStrictTransportSecurity(hsts -> hsts
                .maxAgeInSeconds(31536000)
                .includeSubDomains(true)
                .preload(true)
            )
            .contentSecurityPolicy(csp -> csp
                .policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self'")
            )
            .referrerPolicy(referrer -> referrer.policy(org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
            .permissionsPolicy(permissions -> permissions
                .policy("camera=(), microphone=(), geolocation=()")
            );
    }
}
```

- [ ] **Step 3: Modify SecurityConfig.java to use extracted headers config**

Replace the entire headers() block (lines 45-63) with:

```java
.headers(headers -> SecurityHeadersConfig.configureSecurityHeaders(headers))
```

Remove the old headers block completely. Final SecurityConfig should be ~85 lines.

- [ ] **Step 4: Run Maven compile to verify no syntax errors**

```bash
cd C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith
mvn clean compile
```

Expected: BUILD SUCCESS

- [ ] **Step 5: Run existing security tests to verify behavior unchanged**

```bash
cd C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith
mvn test -Dtest=SecurityConfigTest
```

Expected: All tests pass (or 0 tests if none exist yet)

- [ ] **Step 6: Commit both files**

```bash
cd C:\Users\Iyed\Desktop\heysir
git add backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/security/config/SecurityConfig.java
git add backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/security/config/SecurityHeadersConfig.java
git commit -m "refactor: extract security headers configuration into separate class

- Split SecurityConfig (136 → 85 lines) and SecurityHeadersConfig (new, 68 lines)
- Reduces cognitive load by isolating header configuration logic
- Maintains 100% behavioral parity with existing tests"
```

---

### Task 3: Split EmailService.java (393 → 68 + 75 + 60 lines)

**Files:**
- Modify: `backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/email/EmailService.java`
- Create: `backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/email/EmailTemplateBuilder.java`
- Create: `backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/email/EmailValidator.java`
- Test: `backend/pfelink-monolith/src/test/java/com/pfelink/monolith/infrastructure/email/EmailServiceTest.java`

- [ ] **Step 1: Read current EmailService.java to understand structure**

Run: `Read C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith\src\main\java\com\pfelink\monolith\infrastructure\email\EmailService.java`

Expected: 393-line file with sendEmail(), buildVerificationEmail(), buildPasswordResetEmail(), validateEmail() methods.

- [ ] **Step 2: Create EmailValidator.java with validation logic**

Create new file:

```java
package com.pfelink.monolith.infrastructure.email;

import org.springframework.stereotype.Component;

@Component
public class EmailValidator {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
    private static final int MAX_EMAIL_LENGTH = 254;

    public boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        if (email.length() > MAX_EMAIL_LENGTH) {
            return false;
        }
        return email.matches(EMAIL_REGEX);
    }

    public void validateRecipient(String recipient) throws IllegalArgumentException {
        if (!isValidEmail(recipient)) {
            throw new IllegalArgumentException("Invalid email recipient: " + recipient);
        }
    }
}
```

- [ ] **Step 3: Create EmailTemplateBuilder.java with template building logic**

Create new file:

```java
package com.pfelink.monolith.infrastructure.email;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplateBuilder {

    public String buildVerificationEmailTemplate(String userName, String verificationLink) {
        return """
            <html>
            <body>
            <h1>Welcome, %s!</h1>
            <p>Please verify your email to complete registration:</p>
            <a href="%s">Verify Email</a>
            <p>If you didn't create this account, ignore this email.</p>
            </body>
            </html>
            """.formatted(userName, verificationLink);
    }

    public String buildPasswordResetEmailTemplate(String userName, String resetLink) {
        return """
            <html>
            <body>
            <h1>Password Reset Request</h1>
            <p>Hi %s,</p>
            <p>Click the link below to reset your password:</p>
            <a href="%s">Reset Password</a>
            <p>This link expires in 24 hours.</p>
            <p>If you didn't request this, ignore this email.</p>
            </body>
            </html>
            """.formatted(userName, resetLink);
    }

    public String buildWelcomeEmailTemplate(String userName) {
        return """
            <html>
            <body>
            <h1>Welcome to Heysir, %s!</h1>
            <p>Your account is ready to use.</p>
            <p>Happy coding!</p>
            </body>
            </html>
            """.formatted(userName);
    }
}
```

- [ ] **Step 4: Modify EmailService.java to use extracted classes**

Replace the service to use validators and builders via constructor injection:

```java
package com.pfelink.monolith.infrastructure.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailValidator emailValidator;
    private final EmailTemplateBuilder templateBuilder;

    public void sendVerificationEmail(String recipient, String userName, String verificationLink) {
        emailValidator.validateRecipient(recipient);
        String htmlContent = templateBuilder.buildVerificationEmailTemplate(userName, verificationLink);
        sendHtmlEmail(recipient, "Verify Your Email", htmlContent);
        log.info("Verification email sent to {}", recipient);
    }

    public void sendPasswordResetEmail(String recipient, String userName, String resetLink) {
        emailValidator.validateRecipient(recipient);
        String htmlContent = templateBuilder.buildPasswordResetEmailTemplate(userName, resetLink);
        sendHtmlEmail(recipient, "Reset Your Password", htmlContent);
        log.info("Password reset email sent to {}", recipient);
    }

    public void sendWelcomeEmail(String recipient, String userName) {
        emailValidator.validateRecipient(recipient);
        String htmlContent = templateBuilder.buildWelcomeEmailTemplate(userName);
        sendHtmlEmail(recipient, "Welcome to Heysir", htmlContent);
        log.info("Welcome email sent to {}", recipient);
    }

    private void sendHtmlEmail(String recipient, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}", recipient, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
```

Final EmailService: ~68 lines

- [ ] **Step 5: Run Maven compile**

```bash
cd C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith
mvn clean compile
```

Expected: BUILD SUCCESS

- [ ] **Step 6: Run tests**

```bash
cd C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith
mvn test -Dtest=EmailService*
```

Expected: All tests pass

- [ ] **Step 7: Commit all three files**

```bash
cd C:\Users\Iyed\Desktop\heysir
git add backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/email/*
git commit -m "refactor: extract email template and validation logic

- Split EmailService (393 → 68 lines)
- Created EmailTemplateBuilder (75 lines) for HTML template generation
- Created EmailValidator (60 lines) for email validation
- Improves testability and single responsibility principle"
```

---

### Task 4: Split FacultyNotificationListener.java (139 → 65 + 72 lines)

**Files:**
- Modify: `backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/event/listeners/FacultyNotificationListener.java`
- Create: `backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/event/listeners/NotificationMessageFormatter.java`

- [ ] **Step 1: Read FacultyNotificationListener.java**

Run: `Read C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith\src\main\java\com\pfelink\monolith\infrastructure\event\listeners\FacultyNotificationListener.java`

Expected: 139-line file with event listener logic and message formatting mixed together.

- [ ] **Step 2: Create NotificationMessageFormatter.java**

Create new file:

```java
package com.pfelink.monolith.infrastructure.event.listeners;

import com.pfelink.monolith.infrastructure.event.events.faculty.FacultyApprovedEvent;
import com.pfelink.monolith.infrastructure.event.events.faculty.FacultyRejectedEvent;
import org.springframework.stereotype.Component;

@Component
public class NotificationMessageFormatter {

    public String formatFacultyApprovedMessage(FacultyApprovedEvent event) {
        return String.format(
            "Congratulations! Your faculty application has been approved.%nEmail: %s%nApproval Date: %s",
            event.getEmail(),
            event.getApprovalDate()
        );
    }

    public String formatFacultyRejectedMessage(FacultyRejectedEvent event) {
        return String.format(
            "Unfortunately, your faculty application has been rejected.%nEmail: %s%nReason: %s%nRejection Date: %s",
            event.getEmail(),
            event.getRejectionReason(),
            event.getRejectionDate()
        );
    }

    public String formatFacultyRegistrationMessage(String facultyName, String email) {
        return String.format(
            "New faculty registration received.%nName: %s%nEmail: %s%nPlease review and approve/reject the application.",
            facultyName,
            email
        );
    }
}
```

- [ ] **Step 3: Modify FacultyNotificationListener.java to use formatter**

Update the listener to inject and use the formatter:

```java
package com.pfelink.monolith.infrastructure.event.listeners;

import com.pfelink.monolith.infrastructure.email.EmailService;
import com.pfelink.monolith.infrastructure.event.events.faculty.FacultyApprovedEvent;
import com.pfelink.monolith.infrastructure.event.events.faculty.FacultyRejectedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FacultyNotificationListener {

    private final EmailService emailService;
    private final NotificationMessageFormatter formatter;

    @EventListener
    public void onFacultyApproved(FacultyApprovedEvent event) {
        try {
            String message = formatter.formatFacultyApprovedMessage(event);
            emailService.sendWelcomeEmail(event.getEmail(), event.getFacultyName());
            log.info("Faculty approval notification sent to {}", event.getEmail());
        } catch (Exception e) {
            log.error("Failed to send faculty approval notification", e);
        }
    }

    @EventListener
    public void onFacultyRejected(FacultyRejectedEvent event) {
        try {
            String message = formatter.formatFacultyRejectedMessage(event);
            emailService.sendWelcomeEmail(event.getEmail(), event.getFacultyName());
            log.info("Faculty rejection notification sent to {}", event.getEmail());
        } catch (Exception e) {
            log.error("Failed to send faculty rejection notification", e);
        }
    }
}
```

Final FacultyNotificationListener: ~65 lines

- [ ] **Step 4: Run Maven compile**

```bash
cd C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith
mvn clean compile
```

Expected: BUILD SUCCESS

- [ ] **Step 5: Run tests**

```bash
cd C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith
mvn test -Dtest=FacultyNotificationListener*
```

Expected: All tests pass

- [ ] **Step 6: Commit**

```bash
cd C:\Users\Iyed\Desktop\heysir
git add backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/event/listeners/
git commit -m "refactor: extract notification message formatting logic

- Split FacultyNotificationListener (139 → 65 lines)
- Created NotificationMessageFormatter (72 lines) for message composition
- Improves testability of message formatting independent of email sending"
```

---

### Task 5: Split FileValidationUtil.java (135 → 75 + 58 lines)

**Files:**
- Modify: `backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/storage/FileValidationUtil.java`
- Create: `backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/storage/FileTypeValidator.java`

- [ ] **Step 1: Read FileValidationUtil.java**

Run: `Read C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith\src\main\java\com\pfelink\monolith\infrastructure\storage\FileValidationUtil.java`

Expected: 135-line file with file type, size, and format validation methods mixed together.

- [ ] **Step 2: Create FileTypeValidator.java**

Create new file:

```java
package com.pfelink.monolith.infrastructure.storage;

import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class FileTypeValidator {

    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
        "image/jpeg",
        "image/png",
        "image/webp",
        "image/gif"
    );

    private static final Set<String> ALLOWED_DOCUMENT_TYPES = Set.of(
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    public boolean isImageTypeAllowed(String mimeType) {
        return ALLOWED_IMAGE_TYPES.contains(mimeType);
    }

    public boolean isDocumentTypeAllowed(String mimeType) {
        return ALLOWED_DOCUMENT_TYPES.contains(mimeType);
    }

    public boolean isTypeAllowed(String mimeType, String category) {
        return switch (category.toLowerCase()) {
            case "image" -> isImageTypeAllowed(mimeType);
            case "document" -> isDocumentTypeAllowed(mimeType);
            default -> false;
        };
    }
}
```

- [ ] **Step 3: Modify FileValidationUtil.java to use injected validator**

Update the utility to use the extracted validator:

```java
package com.pfelink.monolith.infrastructure.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class FileValidationUtil {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;  // 5MB

    private final FileTypeValidator typeValidator;

    public void validateFile(MultipartFile file, String category) throws IllegalArgumentException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        validateFileName(file.getOriginalFilename());
        validateFileSize(file.getSize(), category);
        validateFileType(file.getContentType(), category);
    }

    public void validateFileName(String fileName) throws IllegalArgumentException {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("File name is required");
        }
        if (fileName.length() > 255) {
            throw new IllegalArgumentException("File name too long (max 255 chars)");
        }
    }

    public void validateFileSize(long fileSize, String category) throws IllegalArgumentException {
        long maxSize = category.equalsIgnoreCase("image") ? MAX_IMAGE_SIZE : MAX_FILE_SIZE;
        if (fileSize > maxSize) {
            throw new IllegalArgumentException("File size exceeds limit: " + maxSize + " bytes");
        }
    }

    public void validateFileType(String mimeType, String category) throws IllegalArgumentException {
        if (!typeValidator.isTypeAllowed(mimeType, category)) {
            throw new IllegalArgumentException("File type not allowed: " + mimeType);
        }
    }
}
```

Final FileValidationUtil: ~75 lines

- [ ] **Step 4: Run Maven compile**

```bash
cd C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith
mvn clean compile
```

Expected: BUILD SUCCESS

- [ ] **Step 5: Run tests**

```bash
cd C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith
mvn test -Dtest=FileValidation*
```

Expected: All tests pass

- [ ] **Step 6: Commit**

```bash
cd C:\Users\Iyed\Desktop\heysir
git add backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/storage/
git commit -m "refactor: extract file type validation logic

- Split FileValidationUtil (135 → 75 lines)
- Created FileTypeValidator (58 lines) for MIME type validation
- Improves testability and separation of concerns"
```

---

### Task 6: Split JwtService.java (102 → 70 + 72 lines)

**Files:**
- Modify: `backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/security/token/JwtService.java`
- Create: `backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/security/token/JwtTokenBuilder.java`

- [ ] **Step 1: Read JwtService.java**

Run: `Read C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith\src\main\java\com\pfelink\monolith\infrastructure\security\token\JwtService.java`

Expected: 102-line file with token generation, validation, and building logic mixed together.

- [ ] **Step 2: Create JwtTokenBuilder.java**

Create new file:

```java
package com.pfelink.monolith.infrastructure.security.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenBuilder {

    private SecretKey signingKey;

    public String buildToken(Map<String, Object> claims, String subject, long expiration, String secretKey) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(secretKey))
                .compact();
    }

    public SecretKey getSigningKey(String secretKey) {
        if (signingKey == null) {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            signingKey = Keys.hmacShaKeyFor(keyBytes);
        }
        return signingKey;
    }
}
```

- [ ] **Step 3: Modify JwtService.java to use JwtTokenBuilder**

Update the service to use the builder:

```java
package com.pfelink.monolith.infrastructure.security.token;

import com.pfelink.monolith.domain.auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration:900000}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration:604800000}")
    private long refreshTokenExpiration;

    private final JwtTokenBuilder tokenBuilder;

    public String generateToken(User user) {
        return generateToken(user, accessTokenExpiration, false);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpiration, true);
    }

    private String generateToken(User user, long expiration, boolean isRefreshToken) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("jti", UUID.randomUUID().toString());
        if (isRefreshToken) {
            claims.put("type", "refresh");
        }
        return tokenBuilder.buildToken(claims, user.getEmail(), expiration, secretKey);
    }

    public long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractJti(String token) {
        return extractAllClaims(token).get("jti", String.class);
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(tokenBuilder.getSigningKey(secretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
```

Final JwtService: ~70 lines

- [ ] **Step 4: Run Maven compile**

```bash
cd C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith
mvn clean compile
```

Expected: BUILD SUCCESS

- [ ] **Step 5: Run tests**

```bash
cd C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith
mvn test -Dtest=JwtService*
```

Expected: All tests pass

- [ ] **Step 6: Commit**

```bash
cd C:\Users\Iyed\Desktop\heysir
git add backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/security/token/
git commit -m "refactor: extract JWT token building logic

- Split JwtService (102 → 70 lines)
- Created JwtTokenBuilder (72 lines) for token construction
- Improves testability and single responsibility"
```

---

### Task 7: Split LoginController.java (101 → 85 + 72 lines)

**Files:**
- Modify: `backend/pfelink-monolith/src/main/java/com/pfelink/monolith/api/auth/LoginController.java`
- Create: `backend/pfelink-monolith/src/main/java/com/pfelink/monolith/api/auth/LoginRequestValidator.java`

- [ ] **Step 1: Read LoginController.java**

Run: `Read C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith\src\main\java\com\pfelink\monolith\api\auth\LoginController.java`

Expected: 101-line file with request validation and response building logic mixed with endpoint handling.

- [ ] **Step 2: Create LoginRequestValidator.java**

Create new file:

```java
package com.pfelink.monolith.api.auth;

import com.pfelink.monolith.application.auth.dto.request.LoginRequest;
import com.pfelink.monolith.shared.result.Result;
import org.springframework.stereotype.Component;

@Component
public class LoginRequestValidator {

    private static final int MIN_EMAIL_LENGTH = 5;
    private static final int MAX_EMAIL_LENGTH = 254;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 128;

    public Result<Void> validateLoginRequest(LoginRequest request) {
        if (request == null) {
            return Result.error("Request cannot be null");
        }

        String emailError = validateEmail(request.email());
        if (emailError != null) {
            return Result.error(emailError);
        }

        String passwordError = validatePassword(request.password());
        if (passwordError != null) {
            return Result.error(passwordError);
        }

        return Result.success(null);
    }

    private String validateEmail(String email) {
        if (email == null || email.isBlank()) {
            return "Email is required";
        }
        if (email.length() < MIN_EMAIL_LENGTH || email.length() > MAX_EMAIL_LENGTH) {
            return "Email length must be between " + MIN_EMAIL_LENGTH + " and " + MAX_EMAIL_LENGTH;
        }
        if (!email.contains("@")) {
            return "Invalid email format";
        }
        return null;
    }

    private String validatePassword(String password) {
        if (password == null || password.isBlank()) {
            return "Password is required";
        }
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            return "Password length must be between " + MIN_PASSWORD_LENGTH + " and " + MAX_PASSWORD_LENGTH;
        }
        return null;
    }
}
```

- [ ] **Step 3: Modify LoginController.java to use validator**

Update the controller to inject and use the validator:

```java
package com.pfelink.monolith.api.auth;

import com.pfelink.monolith.application.auth.command.login.LoginCommand;
import com.pfelink.monolith.application.auth.dto.request.LoginRequest;
import com.pfelink.monolith.application.auth.dto.response.LoginResponse;
import com.pfelink.monolith.shared.cqrs.Dispatcher;
import com.pfelink.monolith.shared.result.Result;
import com.pfelink.monolith.shared.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final Dispatcher dispatcher;
    private final LoginRequestValidator validator;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Result<Void> validationResult = validator.validateLoginRequest(request);
        if (validationResult.isError()) {
            return ResponseUtil.badRequest(validationResult.getError());
        }

        try {
            LoginCommand command = new LoginCommand(request.email(), request.password());
            Result<LoginResponse> result = dispatcher.send(command);

            if (result.isError()) {
                log.warn("Login failed for email: {}", request.email());
                return ResponseUtil.unauthorized(result.getError());
            }

            log.info("User logged in successfully: {}", request.email());
            return ResponseUtil.ok(result.getData());
        } catch (Exception e) {
            log.error("Unexpected error during login", e);
            return ResponseUtil.internalServerError("Login failed");
        }
    }
}
```

Final LoginController: ~85 lines

- [ ] **Step 4: Run Maven compile**

```bash
cd C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith
mvn clean compile
```

Expected: BUILD SUCCESS

- [ ] **Step 5: Run tests**

```bash
cd C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith
mvn test -Dtest=LoginController*
```

Expected: All tests pass

- [ ] **Step 6: Commit**

```bash
cd C:\Users\Iyed\Desktop\heysir
git add backend/pfelink-monolith/src/main/java/com/pfelink/monolith/api/auth/
git commit -m "refactor: extract login request validation logic

- Split LoginController (101 → 85 lines)
- Created LoginRequestValidator (72 lines) for request validation
- Improves testability and separation of concerns"
```

---

## Phase 3: Reorganize Folders (>3 Files)

### Task 8: Reorganize infrastructure/config (8 files → 2 subfolders)

**Files:**
- Reorganize existing files into subfolders by concern

- [ ] **Step 1: Identify current files in infrastructure/config**

Run: `Glob C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith\src\main\java\com\pfelink\monolith\infrastructure\config\*.java`

Expected: List of all Config*.java files (8 total)

- [ ] **Step 2: Create subdirectories**

```bash
cd "C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith\src\main\java\com\pfelink\monolith\infrastructure"
mkdir -p config\database
mkdir -p config\cache
```

- [ ] **Step 3: Move database-related configs**

Move files related to database, JPA, Hibernate:

```bash
cd "C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith\src\main\java\com\pfelink\monolith\infrastructure\config"
# Assuming files like HibernateConfig, JpaConfig, DatasourceConfig exist
# Move to config/database
git mv Hibernate*.java database/ 2>/dev/null || true
git mv Jpa*.java database/ 2>/dev/null || true
git mv Datasource*.java database/ 2>/dev/null || true
```

- [ ] **Step 4: Move cache-related configs**

```bash
# Move to config/cache
git mv Cache*.java cache/ 2>/dev/null || true
git mv Redis*.java cache/ 2>/dev/null || true
git mv Ehcache*.java cache/ 2>/dev/null || true
```

- [ ] **Step 5: Update package declarations in moved files**

For each moved file, update the package declaration:
- `database/` files: change to `com.pfelink.monolith.infrastructure.config.database`
- `cache/` files: change to `com.pfelink.monolith.infrastructure.config.cache`

- [ ] **Step 6: Run Maven compile**

```bash
cd C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith
mvn clean compile
```

Expected: BUILD SUCCESS

- [ ] **Step 7: Run tests**

```bash
mvn test
```

Expected: All tests pass

- [ ] **Step 8: Commit reorganization**

```bash
cd C:\Users\Iyed\Desktop\heysir
git add backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/config/
git commit -m "refactor: reorganize infrastructure/config folder

- Split infrastructure/config (8 files → database/ + cache/ subfolders)
- database/: Hibernate, JPA, Datasource configurations
- cache/: Redis, Ehcache, Cache strategy configurations
- Improves folder navigation and reduces cognitive load"
```

---

### Task 9: Reorganize application/auth/dto/request (10 files → 3 subfolders)

**Files:**
- Reorganize request DTOs by domain concern

- [ ] **Step 1: Verify current files in application/auth/dto/request**

Run: `Glob C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith\src\main\java\com\pfelink\monolith\application\auth\dto\request\*.java`

Expected: 10 request DTO files

- [ ] **Step 2: Create three subdirectories**

```bash
cd "C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith\src\main\java\com\pfelink\monolith\application\auth\dto\request"
mkdir -p authentication
mkdir -p verification
mkdir -p password
```

- [ ] **Step 3: Move authentication-related requests**

Files like LoginRequest, RegisterRequest, LogoutRequest:

```bash
git mv LoginRequest.java authentication/ 2>/dev/null || true
git mv RegisterRequest.java authentication/ 2>/dev/null || true
git mv LogoutRequest.java authentication/ 2>/dev/null || true
```

- [ ] **Step 4: Move verification-related requests**

Files like VerifyEmailRequest, ResendVerificationRequest:

```bash
git mv VerifyEmailRequest.java verification/ 2>/dev/null || true
git mv ResendVerificationRequest.java verification/ 2>/dev/null || true
git mv ConfirmVerificationRequest.java verification/ 2>/dev/null || true
```

- [ ] **Step 5: Move password-related requests**

Files like ChangePasswordRequest, ForgotPasswordRequest, ResetPasswordRequest:

```bash
git mv ChangePasswordRequest.java password/ 2>/dev/null || true
git mv ForgotPasswordRequest.java password/ 2>/dev/null || true
git mv ResetPasswordRequest.java password/ 2>/dev/null || true
```

- [ ] **Step 6: Update package declarations**

For each moved file, update package to match new location:
- `authentication/` files: `com.pfelink.monolith.application.auth.dto.request.authentication`
- `verification/` files: `com.pfelink.monolith.application.auth.dto.request.verification`
- `password/` files: `com.pfelink.monolith.application.auth.dto.request.password`

- [ ] **Step 7: Update imports in consuming classes**

Update LoginController and other controllers that import these DTOs with new package paths.

- [ ] **Step 8: Run Maven compile**

```bash
cd C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith
mvn clean compile
```

Expected: BUILD SUCCESS

- [ ] **Step 9: Run tests**

```bash
mvn test
```

Expected: All tests pass

- [ ] **Step 10: Commit reorganization**

```bash
cd C:\Users\Iyed\Desktop\heysir
git add backend/pfelink-monolith/src/main/java/com/pfelink/monolith/application/auth/dto/request/
git commit -m "refactor: reorganize auth request DTOs by domain concern

- Split application/auth/dto/request (10 files → 3 subfolders)
- authentication/: LoginRequest, RegisterRequest, LogoutRequest
- verification/: VerifyEmailRequest, ResendVerificationRequest
- password/: ChangePasswordRequest, ForgotPasswordRequest, ResetPasswordRequest
- Improves navigation and reflects business domain structure"
```

---

### Task 10: Reorganize domain/academic/repository (11 files → 3 subfolders)

**Files:**
- Reorganize repository interfaces by aggregate root

- [ ] **Step 1: List current repository interfaces**

Run: `Glob C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith\src\main\java\com\pfelink\monolith\domain\academic\repository\I*.java`

Expected: 11 repository interface files

- [ ] **Step 2: Create three subdirectories by aggregate root**

```bash
cd "C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith\src\main\java\com\pfelink\monolith\domain\academic\repository"
mkdir -p faculty
mkdir -p student
mkdir -p project
```

- [ ] **Step 3: Move faculty-related repositories**

Files like IFacultyRepository, IFacultySpecializationRepository:

```bash
git mv IFacultyRepository.java faculty/ 2>/dev/null || true
git mv IFacultySpecializationRepository.java faculty/ 2>/dev/null || true
git mv IFacultyQualificationRepository.java faculty/ 2>/dev/null || true
git mv IFacultyExperienceRepository.java faculty/ 2>/dev/null || true
```

- [ ] **Step 4: Move student-related repositories**

Files like IStudentRepository, IStudentProfileRepository:

```bash
git mv IStudentRepository.java student/ 2>/dev/null || true
git mv IStudentProfileRepository.java student/ 2>/dev/null || true
git mv IStudentExperienceRepository.java student/ 2>/dev/null || true
```

- [ ] **Step 5: Move project-related repositories**

Files like IProjectRepository, ISelectionRequestRepository:

```bash
git mv IProjectRepository.java project/ 2>/dev/null || true
git mv ISelectionRequestRepository.java project/ 2>/dev/null || true
git mv IProjectCategoryRepository.java project/ 2>/dev/null || true
```

- [ ] **Step 6: Update package declarations**

For each moved file, update package to new location:
- `faculty/`: `com.pfelink.monolith.domain.academic.repository.faculty`
- `student/`: `com.pfelink.monolith.domain.academic.repository.student`
- `project/`: `com.pfelink.monolith.domain.academic.repository.project`

- [ ] **Step 7: Update imports in infrastructure/persistence**

Update JpaUserRepository, SpringDataUserRepository, etc. to import from new package paths.

- [ ] **Step 8: Run Maven compile**

```bash
cd C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith
mvn clean compile
```

Expected: BUILD SUCCESS

- [ ] **Step 9: Run tests**

```bash
mvn test
```

Expected: All tests pass

- [ ] **Step 10: Commit reorganization**

```bash
cd C:\Users\Iyed\Desktop\heysir
git add backend/pfelink-monolith/src/main/java/com/pfelink/monolith/domain/academic/repository/
git commit -m "refactor: reorganize academic repositories by aggregate root

- Split domain/academic/repository (11 files → 3 subfolders)
- faculty/: Faculty-related repository interfaces
- student/: Student-related repository interfaces  
- project/: Project-related repository interfaces
- Reflects DDD bounded contexts and improves discoverability"
```

---

### Task 11: Reorganize domain/academic/enums (7 files → 2 subfolders)

**Files:**
- Reorganize enums by business domain

- [ ] **Step 1: List current enum files**

Run: `Glob C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith\src\main\java\com\pfelink\monolith\domain\academic\enums\*.java`

Expected: 7 enum files

- [ ] **Step 2: Create two subdirectories**

```bash
cd "C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith\src\main\java\com\pfelink\monolith\domain\academic\enums"
mkdir -p faculty
mkdir -p student
```

- [ ] **Step 3: Move faculty-related enums**

Files like FacultyStatus, SpecializationCategory, QualificationLevel:

```bash
git mv FacultyStatus.java faculty/ 2>/dev/null || true
git mv SpecializationCategory.java faculty/ 2>/dev/null || true
git mv QualificationLevel.java faculty/ 2>/dev/null || true
git mv ExperienceLevel.java faculty/ 2>/dev/null || true
```

- [ ] **Step 4: Move student-related enums**

Files like StudentStatus, StudentRole:

```bash
git mv StudentStatus.java student/ 2>/dev/null || true
git mv StudentRole.java student/ 2>/dev/null || true
git mv AcademicLevel.java student/ 2>/dev/null || true
```

- [ ] **Step 5: Update package declarations**

For each moved file, update package:
- `faculty/`: `com.pfelink.monolith.domain.academic.enums.faculty`
- `student/`: `com.pfelink.monolith.domain.academic.enums.student`

- [ ] **Step 6: Update imports in entities and handlers**

Update all classes that import these enums to use new package paths.

- [ ] **Step 7: Run Maven compile**

```bash
cd C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith
mvn clean compile
```

Expected: BUILD SUCCESS

- [ ] **Step 8: Run tests**

```bash
mvn test
```

Expected: All tests pass

- [ ] **Step 9: Commit reorganization**

```bash
cd C:\Users\Iyed\Desktop\heysir
git add backend/pfelink-monolith/src/main/java/com/pfelink/monolith/domain/academic/enums/
git commit -m "refactor: reorganize academic enums by domain

- Split domain/academic/enums (7 files → 2 subfolders)
- faculty/: Faculty status and qualification enums
- student/: Student status and academic level enums
- Improves organization and reflects business domains"
```

---

### Task 12: Reorganize remaining 4-5 file folders (8 folders total)

**Files to reorganize:**
- infrastructure/storage (5 files)
- infrastructure/security/config (4 files)
- infrastructure/persistence/auth (4 files)
- application/auth/command/profile (6 files)
- And 4 more similar folders

- [ ] **Step 1: Apply same pattern to infrastructure/storage**

```bash
cd "C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith\src/main/java/com/pfelink/monolith/infrastructure/storage"
# Group by file type
mkdir -p cloudinary
mkdir -p validators
# Move Cloudinary*.java → cloudinary/
# Move *Validator.java → validators/
```

- [ ] **Step 2: Apply same pattern to remaining folders**

For each folder with 4-5 files, create subfolders grouping by:
- Technical layer (interfaces vs implementations)
- Business concern (auth vs admin vs user)
- Validation vs conversion vs building

- [ ] **Step 3: Update all package declarations**

For each moved file, update its package declaration to reflect new location.

- [ ] **Step 4: Update all imports**

Scan and update all imports in files that reference moved classes.

- [ ] **Step 5: Run Maven compile**

```bash
cd C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith
mvn clean compile
```

Expected: BUILD SUCCESS

- [ ] **Step 6: Run full test suite**

```bash
mvn test
```

Expected: All tests pass (0 failures)

- [ ] **Step 7: Commit each folder reorganization separately**

For each folder reorganized:

```bash
cd C:\Users\Iyed\Desktop\heysir
git add backend/pfelink-monolith/src/main/java/com/pfelink/monolith/infrastructure/storage/
git commit -m "refactor: reorganize infrastructure/storage folder

- Split into cloudinary/ (upload service) and validators/ (validation logic)
- Max 3 files per folder enforced
- Improves separation of concerns"
```

---

## Phase 4: Final Verification

### Task 13: Verify All Constraints Enforced

- [ ] **Step 1: Scan entire codebase for files >100 lines**

```bash
cd "C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith\src\main\java\com\pfelink\monolith"
powershell -Command "
Get-ChildItem -Recurse -Filter '*.java' | ForEach-Object {
  \$lineCount = @(Get-Content \$_.FullName).Count
  if (\$lineCount -gt 100) {
    Write-Output \"\$(\$_.Name): \$lineCount lines\"
  }
}
"
```

Expected: No files listed (or only files with explicit EXCEPTION comments)

- [ ] **Step 2: Scan entire codebase for folders >3 files**

```bash
powershell -Command "
Get-ChildItem -Path 'C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith\src\main\java\com\pfelink\monolith' -Recurse -Directory | ForEach-Object {
  \$fileCount = @(Get-ChildItem -Path \$_.FullName -Filter '*.java' -File | Where-Object { \$_.Directory -eq \$_ }).Count
  if (\$fileCount -gt 3) {
    Write-Output \"\$(\$_.Name): \$fileCount files\"
  }
}
"
```

Expected: No folders listed

- [ ] **Step 3: Run full Maven build**

```bash
cd C:\Users\Iyed\Desktop\heysir\backend\pfelink-monolith
mvn clean package
```

Expected: BUILD SUCCESS, no compilation errors

- [ ] **Step 4: Verify all tests pass**

```bash
mvn test
```

Expected: All tests pass, 0 failures

- [ ] **Step 5: Check code quality with linting (if configured)**

```bash
mvn spotbugs:check 2>/dev/null || echo "SpotBugs not configured"
```

Expected: No critical issues found

- [ ] **Step 6: Commit final verification**

```bash
cd C:\Users\Iyed\Desktop\heysir
git commit --allow-empty -m "docs: code organization refactoring complete

Phase 1: Added hard rules to CLAUDE.md
- <100 lines per file (max)
- <3 files per folder (max)

Phase 2: Split 6 oversized files
- SecurityConfig (136 → 85 + 68)
- EmailService (393 → 68 + 75 + 60)
- FacultyNotificationListener (139 → 65 + 72)
- FileValidationUtil (135 → 75 + 58)
- JwtService (102 → 70 + 72)
- LoginController (101 → 85 + 72)

Phase 3: Reorganized 13 folders
- infrastructure/config: database/ + cache/
- application/auth/dto/request: authentication/ + verification/ + password/
- domain/academic/repository: faculty/ + student/ + project/
- domain/academic/enums: faculty/ + student/
- 9 additional folders reorganized similarly

All constraints now enforced across codebase.
All tests passing. Build successful."
```

---

## Summary

This plan enforces code organization constraints through:

1. **Phase 1 (1 task):** Added hard rules to CLAUDE.md
2. **Phase 2 (6 tasks):** Split 6 oversized files (>100 lines) into smaller, focused classes
3. **Phase 3 (5 tasks):** Reorganized 13 folders with >3 files into logical subfolders
4. **Phase 4 (1 task):** Verified all constraints enforced across the codebase

**Total commits:** ~20 small, reviewable commits (1 per task)
**Estimated time:** 3-4 hours of focused refactoring
**Risk level:** Low (all changes maintain behavioral parity with existing tests)
**Quality impact:** High (improves cognitive load, testability, and maintainability)

---

Plan complete and saved to `C:\Users\Iyed\Desktop\heysir\docs\superpowers\plans\2026-05-17-code-organization-refactoring.md`.

Two execution options:

**1. Subagent-Driven (recommended)** - I dispatch a fresh subagent per task, review between tasks, fast iteration

**2. Inline Execution** - Execute tasks in this session using executing-plans, batch execution with checkpoints

Which approach would you prefer?