# Frontend-Backend Endpoint Mismatches & Issues
**Date:** May 7, 2026  
**Priority:** 🔴 CRITICAL - These issues will cause frontend failures

---

## Summary

The frontend is calling **9+ endpoints** that either:
1. Don't exist in the backend
2. Have incorrect path versions (missing `/v1/`)
3. Haven't been implemented yet

---

## 🔴 CRITICAL ISSUES (Application Breaking)

### Issue #1: Missing Student Profile Management
**Impact:** Student profile update feature will fail  
**Severity:** 🔴 CRITICAL

**Frontend expects:**
```
PUT /api/profile/student
PUT /api/profile/advisor
POST /api/profile/change-password
```

**Backend status:** ❌ Not implemented  

**Fix required:**
- Implement profile update endpoints in backend
- OR Update frontend to remove these features

**Affected Frontend Files:**
- `frontend/src/features/auth/components/*/UpdateProfile.tsx` (assumed)
- `frontend/src/services/auth.service.ts` lines 170-197

---

### Issue #2: Missing Admin Student Approval Workflow
**Impact:** Admin dashboard student approval feature broken  
**Severity:** 🔴 CRITICAL

**Frontend expects:**
```
GET /api/admin/students/pending
POST /api/admin/students/{id}/approve
POST /api/admin/students/{id}/reject
```

**Backend status:** ❌ Not implemented  

**Current backend has:**
- `ApproveUserCommandHandler` (exists but no endpoint)
- `GetPendingUsersQueryHandler` (exists but no endpoint)

**Fix required:**
- Create StudentApprovalController
- Implement GET pending students
- Implement approve/reject endpoints

**Affected Frontend Files:**
- `frontend/src/services/auth.service.ts` lines 203-230
- `frontend/src/features/admin/pages/UserManagement.tsx`

---

## ⚠️ PATH MISMATCH ISSUES (API Version Inconsistency)

### Issue #3: Inconsistent API Versioning
**Impact:** 404 errors on all academic/storage endpoints  
**Severity:** ⚠️ HIGH

Frontend is using incorrect paths:

| Module | Frontend Calls | Backend Provides | Status |
|--------|---|---|---|
| **Projects** | `/api/projects/...` | `/api/v1/projects/...` | ❌ Mismatch |
| **Faculties** | `/api/faculties/...` | `/api/v1/faculties/...` | ❌ Mismatch |
| **Students** | `/api/students/...` | `/api/v1/students/...` | ❌ Mismatch |
| **Advisors** | `/api/advisors/...` | `/api/v1/advisors/...` | ❌ Mismatch |
| **Uploads** | `/api/uploads/...` | `/api/v1/uploads/...` | ❌ Mismatch |
| **Selection** | `/api/selection-requests/...` | `/api/v1/selection-requests/...` | ❌ Mismatch |

**Frontend Files Affected:**
- All service files that make API calls
- All React components using these services

**Fix required:**
- Update all frontend API calls to include `/v1/`
- Example: `'/api/projects/create'` → `'/api/v1/projects'`

---

## 📋 Detailed Issue Breakdown

### Project Management Issues

**File:** `frontend/src/features/pfe/**/*.tsx`

**Current Issues:**
```typescript
// Frontend code (WRONG)
api.post('/api/projects', ...)              // ❌ Should be /api/v1/projects
api.get('/api/projects/me', ...)            // ❌ Should be /api/v1/projects/me
api.post('/api/projects/invite', ...)       // ❌ Should be /api/v1/projects/invite
api.get('/api/projects/invitations/me', ...) // ❌ Should be /api/v1/projects/invitations/me
api.post('/api/projects/invitations/{id}/accept', ...) // ❌
api.post('/api/projects/invitations/{id}/reject', ...) // ❌
```

**Backend provides:**
```
POST   /api/v1/projects
GET    /api/v1/projects/me
POST   /api/v1/projects/invite
GET    /api/v1/projects/invitations/me
POST   /api/v1/projects/invitations/{id}/accept
POST   /api/v1/projects/invitations/{id}/reject
```

---

### Faculty Management Issues

**File:** `frontend/src/features/admin/**/*.tsx`

**Current Issues:**
```typescript
// Frontend code (WRONG)
api.get('/api/faculties', ...)              // ❌ Should be /api/v1/faculties
api.post('/api/faculties', ...)             // ❌ Should be /api/v1/faculties
```

**Backend provides:**
```
GET    /api/v1/faculties?page=0&size=20
POST   /api/v1/faculties
```

---

### Student Profile Issues

**File:** `frontend/src/services/auth.service.ts` lines 170-197

**Current Issues:**
```typescript
// profileService.updateStudentProfile
api.put('/api/profile/student', data)       // ❌ Endpoint doesn't exist

// profileService.updateAdvisorProfile
api.put('/api/profile/advisor', data)       // ❌ Endpoint doesn't exist

// profileService.changePassword
api.post('/api/profile/change-password', ...) // ❌ Endpoint doesn't exist
```

**Solution:**
Either:
1. Implement these endpoints in backend
2. Remove these features from frontend
3. Use alternative endpoints (e.g., `/api/v1/students/{id}` for PUT)

---

### Student Search Issues

**File:** `frontend/src/features/student/**/*.tsx`

**Current Issues:**
```typescript
api.get('/api/students/search', {params: {email}}) // ❌ Should be /api/v1/students/search
```

**Backend provides:**
```
GET /api/v1/students/search?email=...
```

---

### Upload/Storage Issues

**File:** `frontend/src/services/storage.service.ts` (assumed)

**Current Issues:**
```typescript
api.post('/api/uploads/draft', formData)    // ❌ Should be /api/v1/uploads/draft
api.delete('/api/uploads/draft/{id}', ...)  // ❌ Should be /api/v1/uploads/draft/{id}
```

**Backend provides:**
```
POST   /api/v1/uploads/draft
DELETE /api/v1/uploads/draft/{publicId}
```

---

### Advisor Discovery Issues

**File:** `frontend/src/features/student/**/*.tsx`

**Current Issues:**
```typescript
api.get('/api/advisors/faculty/{facultyId}', ...) // ❌ Should be /api/v1/advisors/faculty/{facultyId}
```

**Backend provides:**
```
GET /api/v1/advisors/faculty/{facultyId}
```

---

### Selection Request Issues

**File:** `frontend/src/features/academic/**/*.tsx`

**Current Issues:**
```typescript
api.post('/api/selection-requests', ...)    // ❌ Should be /api/v1/selection-requests
api.get('/api/selection-requests/student/me', ...) // ❌ Should be /api/v1/selection-requests/student/{userId}
api.put('/api/selection-requests/{id}/approve', ...) // ❌ Should be /api/v1/selection-requests/{id}/approve
```

**Backend provides:**
```
POST   /api/v1/selection-requests
GET    /api/v1/selection-requests/student/{userId}
PUT    /api/v1/selection-requests/{id}/approve
PUT    /api/v1/selection-requests/{id}/reject
```

---

## 🛠️ Fix Strategy

### Step 1: Quick Wins (Path Updates) - 2-3 hours
Update all frontend API calls to use `/v1/` prefix:

1. **Identify all affected files:**
```bash
grep -r "'/api/" frontend/src --include="*.ts" --include="*.tsx" | grep -v "/api/auth" | grep -v "/api/admin"
```

2. **Create API client constants:**
```typescript
// frontend/src/config/endpoints.ts
export const API_ENDPOINTS = {
  PROJECTS: '/api/v1/projects',
  FACULTIES: '/api/v1/faculties',
  STUDENTS: '/api/v1/students',
  ADVISORS: '/api/v1/advisors',
  UPLOADS: '/api/v1/uploads',
  SELECTION_REQUESTS: '/api/v1/selection-requests',
};
```

3. **Update service files systematically:**
```typescript
// FROM:
api.post('/api/projects', ...)

// TO:
api.post(API_ENDPOINTS.PROJECTS, ...)
```

### Step 2: Missing Endpoints (Backend) - 4-5 hours
Implement missing endpoints in backend:

1. **Create StudentApprovalController:**
```java
@RestController
@RequestMapping("/api/admin/students")
public class StudentApprovalController {
    @GetMapping("/pending")
    public ResponseEntity<?> getPendingStudents() { ... }
    
    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveStudent(@PathVariable UUID id) { ... }
    
    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectStudent(@PathVariable UUID id) { ... }
}
```

2. **Create or extend StudentController:**
```java
@PutMapping("/{id}")
public ResponseEntity<?> updateStudent(@PathVariable UUID id, @RequestBody UpdateStudentRequest req) { ... }
```

3. **Create AdvisorController update:**
```java
@PutMapping("/{id}")
public ResponseEntity<?> updateAdvisor(@PathVariable UUID id, @RequestBody UpdateAdvisorRequest req) { ... }
```

4. **Create password endpoints:**
```java
@PostMapping("/change-password")
public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest req) { ... }
```

### Step 3: Missing Service Methods (Frontend) - 1-2 hours
Update frontend service definitions:

```typescript
// frontend/src/services/admin.service.ts
export const adminService = {
  getPendingStudents: async () => {...},
  approveStudent: async (id) => {...},
  rejectStudent: async (id, reason) => {...},
};

// frontend/src/services/profile.service.ts
export const profileService = {
  updateStudentProfile: async (data) => {...},
  updateAdvisorProfile: async (data) => {...},
  changePassword: async (data) => {...},
};
```

### Step 4: Testing - 2-3 hours
- Test each endpoint individually with Postman/cURL
- Test complete user flows (signup, login, project creation, etc.)
- Verify token refresh works correctly
- Check error handling

---

## 📝 Implementation Checklist

### Frontend Updates Required

- [ ] Update project endpoint paths in `pfe` feature
- [ ] Update faculty endpoint paths in `admin` feature
- [ ] Update student endpoint paths in `student` feature
- [ ] Update advisor endpoint paths in `student` feature
- [ ] Update selection request endpoint paths in `academic` feature
- [ ] Update upload endpoint paths in storage service
- [ ] Create/update profile service methods
- [ ] Create/update admin student service methods
- [ ] Test all components after path updates

### Backend Implementation Required

- [ ] Create StudentApprovalController
- [ ] Implement GET /api/admin/students/pending
- [ ] Implement POST /api/admin/students/{id}/approve
- [ ] Implement POST /api/admin/students/{id}/reject
- [ ] Add PUT /api/v1/students/{id} for profile updates
- [ ] Add PUT /api/v1/advisors/{id} for profile updates
- [ ] Add password change endpoint
- [ ] Write tests for all new endpoints
- [ ] Update Swagger/OpenAPI documentation

---

## 🔍 Testing These Endpoints

### Using cURL

**Test Projects Endpoint:**
```bash
curl -X GET http://localhost:8081/api/v1/projects/me \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Test Faculties Endpoint:**
```bash
curl -X GET "http://localhost:8081/api/v1/faculties?page=0&size=20"
```

**Test Uploads Endpoint:**
```bash
curl -X POST http://localhost:8081/api/v1/uploads/draft \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -F "file=@path/to/file.pdf"
```

### Using Swagger UI

Visit: `http://localhost:8081/swagger-ui.html`

All endpoints should be listed and testable from there.

---

## Priority Order

**Must Fix First (Blocking Users):**
1. Fix `/api/v1/` path mismatches (quick 2-3 hour fix)
2. Implement missing student approval endpoints (4-5 hours)

**Can Defer (Nice to Have):**
1. Profile update endpoints
2. Password change endpoints

---

## Estimated Time to Fix

| Task | Time | Priority |
|------|------|----------|
| Update all API paths to `/v1/` | 2-3 hours | 🔴 CRITICAL |
| Implement admin student approval | 3-4 hours | 🔴 CRITICAL |
| Implement profile updates | 2-3 hours | 🟡 HIGH |
| Implement password change | 1-2 hours | 🟡 HIGH |
| Full testing suite | 3-4 hours | 🟡 HIGH |
| **Total** | **11-16 hours** | - |

---

**Document Generated:** May 7, 2026  
**Last Updated:** May 7, 2026  
**Prepared for:** Development Team
