# Import Update Guide: Code Organization Refactoring

**Purpose:** Help resolve compilation errors from folder reorganizations where package names changed.

**Status:** Required after pulling the refactoring commits
**Severity:** HIGH - Code won't compile until imports are updated
**Effort:** ~30 minutes with automated search/replace

---

## Quick Start

Run this command to find all import errors:

```bash
cd backend/pfelink-monolith
mvn clean compile 2>&1 | grep "cannot find symbol" | head -20
```

---

## Changed Imports by Folder

### **1. application/auth/dto/request/** ⭐ HIGHEST PRIORITY

**Pattern Change:**
```
OLD: import com.pfelink.monolith.application.auth.dto.request.LoginRequest;
NEW: import com.pfelink.monolith.application.auth.dto.request.authentication.LoginRequest;
```

**Files Affected:**
- LoginController.java (already has some updates)
- All command handlers in `application/auth/command/`
- All services accepting these request types

**Complete Mapping:**
| Old Import | New Import | Subfolder |
|-----------|-----------|-----------|
| `.request.LoginRequest` | `.request.authentication.LoginRequest` | authentication/ |
| `.request.RegisterAdvisorRequest` | `.request.authentication.RegisterAdvisorRequest` | authentication/ |
| `.request.RegisterStudentRequest` | `.request.authentication.RegisterStudentRequest` | authentication/ |
| `.request.ChangePasswordRequest` | `.request.password.ChangePasswordRequest` | password/ |
| `.request.ForgotPasswordRequest` | `.request.password.ForgotPasswordRequest` | password/ |
| `.request.ResetPasswordRequest` | `.request.password.ResetPasswordRequest` | password/ |
| `.request.RefreshTokenRequest` | `.request.tokens.RefreshTokenRequest` | tokens/ |
| `.request.VerifyOtpRequest` | `.request.tokens.VerifyOtpRequest` | tokens/ |
| `.request.UpdateAdvisorProfileRequest` | `.request.profile.UpdateAdvisorProfileRequest` | profile/ |
| `.request.UpdateStudentProfileRequest` | `.request.profile.UpdateStudentProfileRequest` | profile/ |

**Search & Replace in IDE:**

In IntelliJ/VS Code, use Find & Replace (Ctrl+H):

1. Find: `import com.pfelink.monolith.application.auth.dto.request.LoginRequest;`
   Replace: `import com.pfelink.monolith.application.auth.dto.request.authentication.LoginRequest;`

2. Find: `import com.pfelink.monolith.application.auth.dto.request.RefreshTokenRequest;`
   Replace: `import com.pfelink.monolith.application.auth.dto.request.tokens.RefreshTokenRequest;`

3. Find: `import com.pfelink.monolith.application.auth.dto.request.(\w+PasswordRequest);`
   Replace: `import com.pfelink.monolith.application.auth.dto.request.password.$1;` (use regex)

4. Find: `import com.pfelink.monolith.application.auth.dto.request.Update(\w+ProfileRequest);`
   Replace: `import com.pfelink.monolith.application.auth.dto.request.profile.Update$1ProfileRequest;` (use regex)

**Files to Update:**
```
backend/pfelink-monolith/src/main/java/com/pfelink/monolith/
├── api/auth/LoginController.java ✅ (partially done)
├── api/auth/RegistrationController.java
├── api/auth/admin/AdminController.java
├── application/auth/command/login/LoginCommandHandler.java
├── application/auth/command/register/*/RegisterCommandHandler.java
├── application/auth/command/change_password/ChangePasswordCommandHandler.java
├── application/auth/command/refresh_token/RefreshTokenCommandHandler.java
└── infrastructure/event/listeners/**/*Listener.java
```

---

### **2. domain/academic/enums/** ⭐ HIGH PRIORITY

**Pattern Changes:**

```
OLD: import com.pfelink.monolith.domain.academic.enums.AdvisorRole;
NEW: import com.pfelink.monolith.domain.academic.enums.faculty.AdvisorRole;

OLD: import com.pfelink.monolith.domain.academic.enums.StudentStatus;
NEW: import com.pfelink.monolith.domain.academic.enums.student.StudentStatus;

OLD: import com.pfelink.monolith.domain.academic.enums.ProjectStatus;
NEW: import com.pfelink.monolith.domain.academic.enums.project.ProjectStatus;
```

**Complete Mapping:**

| Old Import | New Import | Subfolder |
|-----------|-----------|-----------|
| `.enums.AdvisorRole` | `.enums.faculty.AdvisorRole` | faculty/ |
| `.enums.AssignmentStatus` | `.enums.faculty.AssignmentStatus` | faculty/ |
| `.enums.StudentStatus` | `.enums.student.StudentStatus` | student/ |
| `.enums.VerificationStatus` | `.enums.student.VerificationStatus` | student/ |
| `.enums.ProjectStatus` | `.enums.project.ProjectStatus` | project/ |
| `.enums.ProjectInvitationStatus` | `.enums.project.ProjectInvitationStatus` | project/ |
| `.enums.SelectionStatus` | `.enums.project.SelectionStatus` | project/ |

**Search & Replace (Regex):**

1. Find: `import com.pfelink.monolith.domain.academic.enums.(AdvisorRole|AssignmentStatus);`
   Replace: `import com.pfelink.monolith.domain.academic.enums.faculty.$1;` (use regex)

2. Find: `import com.pfelink.monolith.domain.academic.enums.(StudentStatus|VerificationStatus);`
   Replace: `import com.pfelink.monolith.domain.academic.enums.student.$1;` (use regex)

3. Find: `import com.pfelink.monolith.domain.academic.enums.(ProjectStatus|ProjectInvitationStatus|SelectionStatus);`
   Replace: `import com.pfelink.monolith.domain.academic.enums.project.$1;` (use regex)

**Files to Update:**
```
Entities using these enums:
├── domain/academic/entity/Faculty.java
├── domain/academic/entity/Assignment.java
├── domain/academic/entity/Student.java
├── domain/academic/entity/Project.java
├── domain/academic/entity/Selection.java

Commands/Handlers:
├── application/academic/command/**/Handler.java
├── application/academic/query/**/QueryHandler.java

Event listeners:
└── infrastructure/event/listeners/**/*Listener.java
```

---

### **3. infrastructure/config/** MEDIUM PRIORITY

**Pattern Changes:**

```
OLD: import com.pfelink.monolith.infrastructure.config.CacheConfig;
NEW: import com.pfelink.monolith.infrastructure.config.cache.CacheConfig;

OLD: import com.pfelink.monolith.infrastructure.config.DataInitializer;
NEW: import com.pfelink.monolith.infrastructure.config.database.DataInitializer;
```

**Complete Mapping:**

| Old Import | New Import | Subfolder |
|-----------|-----------|-----------|
| `.config.CacheConfig` | `.config.cache.CacheConfig` | cache/ |
| `.config.AsyncConfig` | `.config.cache.AsyncConfig` | cache/ |
| `.config.HibernateMetricsConfig` | `.config.database.HibernateMetricsConfig` | database/ |
| `.config.LtreeExtensionConfig` | `.config.database.LtreeExtensionConfig` | database/ |
| `.config.DataInitializer` | `.config.database.DataInitializer` | database/ |
| `.config.CloudinaryConfig` | `.config.integration.CloudinaryConfig` | integration/ |
| `.config.OpenApiConfig` | `.config.integration.OpenApiConfig` | integration/ |
| `.config.WebConfig` | `.config.integration.WebConfig` | integration/ |

**Search & Replace:**

1. Find: `import com.pfelink.monolith.infrastructure.config.(CacheConfig|AsyncConfig);`
   Replace: `import com.pfelink.monolith.infrastructure.config.cache.$1;` (regex)

2. Find: `import com.pfelink.monolith.infrastructure.config.(Hibernate|Ltree|Data);`
   Replace: `import com.pfelink.monolith.infrastructure.config.database.$1;` (regex - partial match)

3. Find: `import com.pfelink.monolith.infrastructure.config.(Cloudinary|OpenApi|Web)Config;`
   Replace: `import com.pfelink.monolith.infrastructure.config.integration.$1Config;` (regex)

**Files to Update:**
```
Main application class:
├── Application.java

Other configuration consumers:
├── infrastructure/persistence/**
└── infrastructure/event/**
```

---

## Finding All Import Errors Systematically

### **Method 1: Maven Compilation (Most Reliable)**

```bash
cd backend/pfelink-monolith

# Compile and capture errors
mvn clean compile 2>&1 | tee compile.log

# Extract import errors
grep "cannot find symbol" compile.log | grep "import" | head -20

# Extract class not found errors
grep "\[ERROR\]" compile.log | grep -i "cannot find symbol" | cut -d' ' -f6-
```

### **Method 2: IDE Quick Fix**

In IntelliJ IDEA:
1. Open **Run → Edit Configurations**
2. Add configuration: **Maven → Clean, Compile**
3. Run and view errors in console
4. Double-click error to navigate to file
5. Use **Alt+Enter** for quick import fix

In VS Code:
1. Install **Extension Pack for Java**
2. Watch the **Problems** tab for import errors
3. Hover over error and click **Quick Fix**

### **Method 3: Find & Replace All At Once**

```bash
# Bash script to update all imports at once
cd backend/pfelink-monolith/src

# Update auth request imports
find . -name "*.java" -type f -exec sed -i \
  's/import com\.pfelink\.monolith\.application\.auth\.dto\.request\.LoginRequest;/import com.pfelink.monolith.application.auth.dto.request.authentication.LoginRequest;/g' {} \;

# Update enum imports
find . -name "*.java" -type f -exec sed -i \
  's/import com\.pfelink\.monolith\.domain\.academic\.enums\.AdvisorRole;/import com.pfelink.monolith.domain.academic.enums.faculty.AdvisorRole;/g' {} \;

# ... repeat for each changed import
```

---

## Priority Order for Fixing Imports

**Fix in this order** (highest impact first):

1. **application/auth/dto/request/** (10 files moved)
   - Affects: LoginController, RegistrationController, all auth command handlers
   - Priority: CRITICAL - application won't start without these

2. **domain/academic/enums/** (7 files moved)
   - Affects: All entities using enums, all command/query handlers
   - Priority: CRITICAL - domain layer core types

3. **infrastructure/config/** (8 files moved)
   - Affects: Configuration loading, dependency injection
   - Priority: HIGH - app startup may fail

4. **Other reorganized folders** (as they're moved)
   - Priority: MEDIUM/LOW

---

## Testing After Import Updates

### **Step 1: Verify Compilation**

```bash
cd backend/pfelink-monolith
mvn clean compile

# Expected: BUILD SUCCESS
```

### **Step 2: Run Unit Tests**

```bash
mvn test

# Expected: All tests pass (behavior unchanged)
```

### **Step 3: Start Application**

```bash
mvn spring-boot:run

# Expected: No startup errors, server ready on port 8081
```

### **Step 4: Verify Functionality**

```bash
# Test login endpoint
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'

# Expected: 200 OK with token response (or 401 if invalid credentials)
```

---

## Troubleshooting Common Errors

### **Error: "cannot find symbol" for request DTOs**

```
[ERROR] .../LoginController.java:[7,49] cannot find symbol
  symbol:   class LoginRequest
  location: package com.pfelink.monolith.application.auth.dto.request
```

**Solution:** Update import to include subfolder:
```java
// OLD ❌
import com.pfelink.monolith.application.auth.dto.request.LoginRequest;

// NEW ✅
import com.pfelink.monolith.application.auth.dto.request.authentication.LoginRequest;
```

---

### **Error: "cannot find symbol" for enums**

```
[ERROR] .../Faculty.java:[15,51] cannot find symbol
  symbol:   class AdvisorRole
  location: package com.pfelink.monolith.domain.academic.enums
```

**Solution:** Update import to include subfolder:
```java
// OLD ❌
import com.pfelink.monolith.domain.academic.enums.AdvisorRole;

// NEW ✅
import com.pfelink.monolith.domain.academic.enums.faculty.AdvisorRole;
```

---

### **Error: "cannot find symbol" for config classes**

```
[ERROR] .../Application.java:[8,41] cannot find symbol
  symbol:   class CacheConfig
  location: package com.pfelink.monolith.infrastructure.config
```

**Solution:** Update import to include subfolder:
```java
// OLD ❌
import com.pfelink.monolith.infrastructure.config.CacheConfig;

// NEW ✅
import com.pfelink.monolith.infrastructure.config.cache.CacheConfig;
```

---

## Automated Fix Script (PowerShell)

Save as `fix-imports.ps1`:

```powershell
# Fix authentication request imports
Get-ChildItem -Path "backend/pfelink-monolith/src" -Filter "*.java" -Recurse | ForEach-Object {
    $content = Get-Content $_.FullName -Raw
    
    # Update auth request imports
    $content = $content -replace 'import com\.pfelink\.monolith\.application\.auth\.dto\.request\.LoginRequest;', `
        'import com.pfelink.monolith.application.auth.dto.request.authentication.LoginRequest;'
    $content = $content -replace 'import com\.pfelink\.monolith\.application\.auth\.dto\.request\.RefreshTokenRequest;', `
        'import com.pfelink.monolith.application.auth.dto.request.tokens.RefreshTokenRequest;'
    
    # Update enum imports
    $content = $content -replace 'import com\.pfelink\.monolith\.domain\.academic\.enums\.AdvisorRole;', `
        'import com.pfelink.monolith.domain.academic.enums.faculty.AdvisorRole;'
    $content = $content -replace 'import com\.pfelink\.monolith\.domain\.academic\.enums\.StudentStatus;', `
        'import com.pfelink.monolith.domain.academic.enums.student.StudentStatus;'
    
    # Update config imports
    $content = $content -replace 'import com\.pfelink\.monolith\.infrastructure\.config\.CacheConfig;', `
        'import com.pfelink.monolith.infrastructure.config.cache.CacheConfig;'
    $content = $content -replace 'import com\.pfelink\.monolith\.infrastructure\.config\.DataInitializer;', `
        'import com.pfelink.monolith.infrastructure.config.database.DataInitializer;'
    
    if ($content -ne (Get-Content $_.FullName -Raw)) {
        Set-Content -Path $_.FullName -Value $content
        Write-Host "✅ Fixed: $($_.Name)"
    }
}
```

Run with:
```powershell
.\fix-imports.ps1
```

---

## Validation Checklist

After updating imports, verify:

- [ ] `mvn clean compile` passes with no errors
- [ ] `mvn test` passes with same pass rate as before
- [ ] `mvn spring-boot:run` starts without errors
- [ ] Application responds to test request (login endpoint)
- [ ] No warning logs about missing imports at startup
- [ ] IDE shows no red squiggles on organized files
- [ ] All reorganized folders have max 3 files

---

## Still Having Issues?

If compilation still fails after updating imports:

1. **Check for wildcard imports:**
   ```bash
   grep "import com.pfelink.monolith.*.\*;" backend/pfelink-monolith/src -r
   ```
   Wildcard imports may not resolve reorganized classes.

2. **Look for inline class references:**
   ```bash
   grep -r "new LoginRequest" backend/pfelink-monolith/src
   grep -r "AdvisorRole\." backend/pfelink-monolith/src
   ```
   These don't need import changes, but verify package names.

3. **Run clean rebuild:**
   ```bash
   mvn clean compile -DskipTests=true
   ```
   Sometimes IDE caches need clearing.

4. **Check for duplicate/conflicting imports:**
   ```bash
   grep -n "import.*LoginRequest" backend/pfelink-monolith/src -r
   ```
   Should show only ONE import per file (the updated one).

---

## Reference: All Moved Files

### **application/auth/dto/request/ (10 files)**
- ✅ authentication/: LoginRequest, RegisterAdvisorRequest, RegisterStudentRequest
- ✅ password/: ChangePasswordRequest, ForgotPasswordRequest, ResetPasswordRequest
- ✅ tokens/: RefreshTokenRequest, VerifyOtpRequest
- ✅ profile/: UpdateAdvisorProfileRequest, UpdateStudentProfileRequest

### **domain/academic/enums/ (7 files)**
- ✅ faculty/: AdvisorRole, AssignmentStatus
- ✅ student/: StudentStatus, VerificationStatus
- ✅ project/: ProjectStatus, ProjectInvitationStatus, SelectionStatus

### **infrastructure/config/ (8 files)**
- ✅ database/: HibernateMetricsConfig, LtreeExtensionConfig, DataInitializer
- ✅ cache/: CacheConfig, AsyncConfig
- ✅ integration/: CloudinaryConfig, OpenApiConfig, WebConfig

---

**Last Updated:** 2026-05-17
**Status:** Complete for Phase 3 reorganizations so far
**Next:** Continue with remaining folder reorganizations
