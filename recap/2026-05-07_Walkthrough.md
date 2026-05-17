# Heysir Integration Walkthrough
**Date:** May 7, 2026 | **Status:** Post-fix, all integrations verified

## User Journeys

### 1. Student Registration → Approval
- Flow: StudentSignup → `/api/auth/signup/student` → email verification → admin sees in `/api/admin/students/pending` → admin approves via `/api/admin/students/{id}/approve`

### 2. Student Creates PFE Project + Invites Friend
- Flow: PFEDashboard → usePFEs.createPFE → POST `/api/v1/projects` → invite via POST `/api/v1/projects/invite` → friend sees in GET `/api/v1/projects/invitations/me`

### 3. Student Selects Advisor
- Flow: AdvisorSelection → GET `/api/v1/students/me` (get facultyId) → GET `/api/v1/advisors/faculty/{id}` → POST `/api/v1/selection-requests` {projectId, advisorProfileId, message}

### 4. Advisor Reviews Requests
- Flow: AdvisorDashboard → GET `/api/v1/selection-requests/advisor/{id}/faculty/{id}` → PUT `/{id}/approve` or `/{id}/reject`

### 5. Admin Manages Faculties
- Flow: FacultyManagement → GET `/api/v1/faculties` → DELETE `/api/admin/faculties/{id}`

### 6. Profile Update
- Flow: PUT `/api/profile/student` or PUT `/api/profile/advisor` → POST `/api/profile/change-password`

## Fixed Issues Summary

| Issue | Before | After |
|-------|-------|-------|
| 1. SelectionRequest DTO | Returns raw JPA entity with lazy relations → LazyInitializationException | Returns SelectionRequestResponse DTO with mapped fields |
| 2. GET /api/admin/users | 404 Not Found | Returns student list via AdminUserController |
| 3. Student Management | 404 for /pending, /approve, /reject | StudentAdminController exposes all three endpoints |
| 4. DELETE /api/admin/faculties/{id} | 404 Not Found | FacultyDeleteController with DeleteFacultyCommand |
| 5. PUT /api/profile/* | 404 Not Found | ProfileController with Update* and ChangePassword commands |
| 6. SubmitSelectionRequest Type | Sends 5 extra fields backend ignores | Fixed to send only {projectId, advisorProfileId, message} |
| 7. usePFEs Mock | console.log only | Wired to projectService.createProject |
| 8. ProjectDetails | Hardcoded data | Uses useMyProject hook |
| 9. AdvisorDashboard | Hardcoded stats | Fetches from GET selection-requests API |
| 10. useAdvisorGallery | Missing selection/isSelecting/handleSelect | Added all return values |

## API Quick Reference

| Method | Path | Auth | Purpose |
|--------|------|------|-------|
| GET | /api/admin/users | ADMIN | List all students |
| GET | /api/admin/students/pending | ADMIN | List pending students |
| POST | /api/admin/students/{id}/approve | ADMIN | Approve student |
| POST | /api/admin/students/{id}/reject | ADMIN | Reject student |
| DELETE | /api/admin/faculties/{id} | ADMIN | Delete faculty |
| PUT | /api/profile/student | STUDENT | Update student profile |
| PUT | /api/profile/advisor | ADVISOR | Update advisor profile |
| POST | /api/profile/change-password | USER | Change password |
| GET | /api/v1/selection-requests/student/{id} | STUDENT | Get student's selection (DTO) |
| POST | /api/v1/selection-requests | STUDENT | Submit advisor selection |
| GET | /api/v1/selection-requests/adveyor/{id}/faculty/{id} | ADVISOR | Get advisor requests |

## New Backend Files Created

- `SelectionRequestResponse.java` - DTO to prevent LazyInitializationException
- `GetStudentSelectionQuery.java` - Updated return type
- `GetStudentSelectionQueryHandler.java` - Maps entity to DTO
- `StudentAdminController.java` - /api/admin/students endpoints
- `AdminUserController.java` - /api/admin/users endpoint
- `DeleteFacultyCommand.java` + Handler - Faculty deletion
- `FacultyDeleteController.java` - DELETE /api/admin/faculties/{id}
- `UpdateStudentProfileRequest.java`, `UpdateAdvisorProfileRequest.java`, `ChangePasswordRequest.java` - DTOs
- `UpdateStudentProfileCommand.java` + Handler
- `UpdateAdvisorProfileCommand.java` + Handler
- `ChangePasswordCommand.java` + Handler
- `ProfileController.java` - PUT /api/profile/*, POST /api/profile/change-password
- Updated `IFacultyRepository.java` - Added existsById, deleteById

## New Frontend Files/Changes

- `academic.types.ts` - Fixed SubmitSelectionRequest interface
- `usePFEs.ts` - Wired to real projectService
- `ProjectDetails.tsx` - Uses useMyProject hook
- `AdvisorDashboard.tsx` - Fetches real selection data
- `AdvisorStats.tsx` - Accepts props for real numbers
- `SupervisionList.tsx` - Accepts real data items
- `RequestsSidebar.tsx` - Accepts real data items
- `useAdvisorGallery.ts` - Added selection, isSelecting, handleSelect