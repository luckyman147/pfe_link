# Backend API Endpoints Documentation & Frontend Gap Analysis

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Document all 13 backend controllers with 59 endpoints (unified GET /api/auth/me for all roles), identify request/response formats, and audit frontend implementation coverage.

**Architecture:** 
- Spring Boot 3.4.2 REST API with CQRS pattern
- JWT authentication via HttpOnly cookies
- OpenAPI/Swagger documentation available at `/swagger-ui.html`
- All endpoints return standardized `Result<T>` wrapper via `ResponseUtil.toResponse()`

**Tech Stack:** Spring Boot, Jakarta Servlet API, Jackson for JSON serialization, Spring Security, CQRS Dispatcher

---

## Complete API Endpoint Inventory

### Category 1: Authentication & Authorization (12 endpoints - unified /api/auth/me for all roles)

#### 1.1 Login Controller (`/api/auth`)

**GET /api/auth/me** ⭐ UNIFIED ENDPOINT FOR ALL ROLES
- **Summary:** Get complete user profile with all role-specific information
- **Authentication:** Required (JWT, any authenticated user)
- **Request:** None
- **Response:** Complete user object with role-specific data
  ```json
  {
    "id": "uuid",
    "email": "user@example.com",
    "fullName": "John Doe",
    "telephone": "+216 XX XXX XXXX",
    "role": "STUDENT|ADVISOR|ADMIN|FACULTY_ADMIN",
    "createdAt": "2026-01-01T00:00:00Z",
    "roleSpecificData": {
      "studentProfile": {
        "facultyId": "uuid",
        "studentCardUrl": "https://...",
        "status": "APPROVED",
        "verificationStatus": "VERIFIED"
      },
      "advisorProfile": {
        "facultyId": "uuid",
        "facultyDomainEmail": "advisor@faculty.edu",
        "assignmentStatus": "APPROVED"
      },
      "facultyProfile": {
        "facultyName": "Faculty of Science",
        "abbreviation": "FS",
        "adminStatus": "ACTIVE"
      }
    }
  }
  ```
- **Status Codes:** 200 OK, 401 Unauthorized
- **Frontend Status:** ✅ Implemented (replaces all role-specific /me endpoints)

**POST /api/auth/login**
- **Summary:** Login with email/password
- **Authentication:** None
- **Request:** 
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```
- **Headers:** `X-Recaptcha-Token` (optional but validated)
- **Response:** `AuthResponseDTO`
  ```json
  {
    "token": "jwt.access.token",
    "refreshToken": "jwt.refresh.token",
    "user": { "id": "uuid", "email": "...", "role": "..." }
  }
  ```
- **Side Effects:** Sets HttpOnly cookies for `access_token` and `refresh_token`
- **Status Codes:** 200 OK, 400/422 Validation Error, 401 Invalid credentials, 403 reCAPTCHA failed
- **Frontend Status:** ✅ Must be implemented (core feature)

**POST /api/auth/logout**
- **Summary:** Logout and blacklist current token
- **Authentication:** Required (JWT)
- **Request:** None
- **Response:** Success message
- **Side Effects:** Clears HttpOnly cookies, blacklists JWT jti
- **Status Codes:** 200 OK
- **Frontend Status:** ✅ Likely implemented

**POST /api/auth/refresh**
- **Summary:** Refresh access token using refresh token
- **Authentication:** None (uses refresh token from cookie or body)
- **Request (Optional Body):**
  ```json
  {
    "refreshToken": "jwt.refresh.token"
  }
  ```
- **OR Cookie:** `refresh_token` cookie value
- **Response:** `TokenResponseDTO`
  ```json
  {
    "accessToken": "new.jwt.access.token",
    "refreshToken": "new.jwt.refresh.token"
  }
  ```
- **Status Codes:** 200 OK, 400 Missing token, 401 Invalid token
- **Frontend Status:** ✅ Likely implemented (auto token refresh)

#### 1.2 Registration Controller (`/api/auth`)

**POST /api/auth/signup/student**
- **Summary:** Register as a student
- **Authentication:** None
- **Request:**
  ```json
  {
    "email": "student@example.com",
    "password": "SecurePass123!",
    "fullName": "John Doe",
    "telephone": "+216 XX XXX XXXX",
    "cinNumber": "12345678",
    "studentCardUrl": "https://cloudinary.url/card.jpg",
    "draftId": "uuid-of-student-card-cloudinary",
    "facultyId": "uuid-of-faculty"
  }
  ```
- **Headers:** `X-Recaptcha-Token` (optional but validated)
- **Response:** Registration result with user ID
- **Status Codes:** 201 Created, 400 Validation, 409 Email exists
- **Frontend Status:** ✅ Likely implemented

**POST /api/auth/signup/advisor**
- **Summary:** Register as an advisor
- **Authentication:** None
- **Request:**
  ```json
  {
    "email": "advisor@faculty.edu",
    "password": "SecurePass123!",
    "fullName": "Dr. Jane Smith",
    "telephone": "+216 XX XXX XXXX",
    "cinNumber": "87654321",
    "cinCardUrl": "https://cloudinary.url/cin.jpg",
    "draftId": "uuid-of-cin-cloudinary",
    "facultyId": "uuid-of-faculty",
    "facultyDomainEmail": "jane.smith@faculty.edu"
  }
  ```
- **Headers:** `X-Recaptcha-Token` (optional but validated)
- **Response:** Registration result with user ID
- **Status Codes:** 201 Created, 400 Validation, 409 Email exists
- **Frontend Status:** ✅ Likely implemented

**POST /api/auth/verify-email**
- **Summary:** Verify email address with token
- **Authentication:** None
- **Request:** Query parameter `token=<email-verification-token>`
- **Response:** Verification success message
- **Status Codes:** 200 OK, 400 Invalid/expired token
- **Frontend Status:** ⚠️ Partially implemented (likely works via email link, may need UI feedback)

#### 1.3 Profile Controller (`/api/profile`)

**PUT /api/profile/student**
- **Summary:** Update student profile
- **Authentication:** Required (JWT, must be STUDENT role)
- **Request:**
  ```json
  {
    "fullName": "John Doe Updated",
    "telephone": "+216 XX XXX XXXX"
  }
  ```
- **Response:** Updated user profile
- **Status Codes:** 200 OK, 400 Validation, 401 Unauthorized
- **Frontend Status:** ⚠️ Needs verification (profile edit form for students)

**PUT /api/profile/advisor**
- **Summary:** Update advisor profile
- **Authentication:** Required (JWT, must be ADVISOR role)
- **Request:**
  ```json
  {
    "fullName": "Dr. Jane Smith Updated",
    "telephone": "+216 XX XXX XXXX"
  }
  ```
- **Response:** Updated user profile
- **Status Codes:** 200 OK, 400 Validation, 401 Unauthorized
- **Frontend Status:** ⚠️ Needs verification (profile edit form for advisors)

**POST /api/profile/change-password**
- **Summary:** Change current password
- **Authentication:** Required (JWT)
- **Request:**
  ```json
  {
    "currentPassword": "oldPassword123",
    "newPassword": "newPassword456"
  }
  ```
- **Response:** Success message
- **Status Codes:** 200 OK, 400 Validation/Current password mismatch, 401 Unauthorized
- **Frontend Status:** ⚠️ Needs verification (change password form)

#### 1.4 Password Controller (`/api/auth`)

**POST /api/auth/forgot-password**
- **Summary:** Request password reset via OTP
- **Authentication:** None
- **Request:**
  ```json
  {
    "email": "user@example.com"
  }
  ```
- **Response:** Success message
- **Side Effects:** Sends OTP via email
- **Status Codes:** 200 OK, 400 Invalid email
- **Frontend Status:** ✅ Likely implemented (forgot password form)

**POST /api/auth/verify-otp**
- **Summary:** Verify OTP code sent to email
- **Authentication:** None
- **Request:**
  ```json
  {
    "email": "user@example.com",
    "otpCode": "123456"
  }
  ```
- **Response:** Verification result (OTP validity confirmed)
- **Status Codes:** 200 OK, 400 Invalid/expired OTP
- **Frontend Status:** ⚠️ Needs verification (OTP verification form)

**POST /api/auth/reset-password**
- **Summary:** Reset password with verified OTP
- **Authentication:** None
- **Request:**
  ```json
  {
    "email": "user@example.com",
    "otpCode": "123456",
    "newPassword": "newPassword789"
  }
  ```
- **Response:** Success message
- **Status Codes:** 200 OK, 400 Invalid OTP or validation error
- **Frontend Status:** ⚠️ Needs verification (password reset form)

---

### Category 2: Academic - Faculties (10 endpoints)

#### 2.1 Faculty Controller (`/api/v1/faculties`)

**GET /api/v1/faculties**
- **Summary:** Get all faculties with pagination
- **Authentication:** None
- **Query Parameters:** 
  - `page` (int, default=0)
  - `size` (int, default=20)
- **Response:** Paginated faculty list
  ```json
  {
    "content": [
      {
        "id": "uuid",
        "name": "Faculty of Science",
        "abbreviation": "FS",
        "email": "info@science.edu",
        "websiteUrl": "https://science.edu",
        "imageUrl": "https://cloudinary.url/logo.jpg",
        "status": "APPROVED",
        "createdAt": "2026-01-01T00:00:00Z"
      }
    ],
    "totalElements": 50,
    "totalPages": 3,
    "currentPage": 0
  }
  ```
- **Status Codes:** 200 OK
- **Frontend Status:** ✅ Likely implemented (faculty list page)

**POST /api/v1/faculties**
- **Summary:** Create new faculty
- **Authentication:** Required (ADMIN only)
- **Request:**
  ```json
  {
    "name": "Faculty of Engineering",
    "abbreviation": "FE",
    "email": "admin@eng.edu",
    "websiteUrl": "https://eng.edu",
    "imageUrl": "https://cloudinary.url/logo.jpg",
    "adminPassword": "AdminPass123!",
    "path": "/faculty/engineering"
  }
  ```
- **Response:** Created faculty ID (UUID)
- **Status Codes:** 201 Created, 400 Validation, 403 Forbidden (non-admin)
- **Frontend Status:** ❌ Missing (admin only feature, likely not in regular app)

#### 2.2 Faculty Approval Controller (`/api/faculty-action`)

**GET /api/faculty-action/approve/{id}**
- **Summary:** Display HTML confirmation page for faculty approval (email link)
- **Authentication:** None
- **Path Parameter:** `id` (UUID of faculty)
- **Response:** HTML page with confirmation button
- **Status Codes:** 200 OK, 404 Faculty not found
- **Frontend Status:** ✅ Implemented (HTML email link)

**POST /api/faculty-action/approve/{id}**
- **Summary:** Approve faculty (form submission from email link)
- **Authentication:** None
- **Path Parameter:** `id` (UUID of faculty)
- **Response:** HTML success page
- **Status Codes:** 200 OK, 400 Error
- **Frontend Status:** ✅ Implemented (HTML form in email)

**GET /api/faculty-action/reject/{id}**
- **Summary:** Display HTML confirmation page for faculty rejection
- **Authentication:** None
- **Path Parameter:** `id` (UUID of faculty)
- **Response:** HTML page with confirmation button
- **Status Codes:** 200 OK, 404 Faculty not found
- **Frontend Status:** ✅ Implemented (HTML email link)

**POST /api/faculty-action/reject/{id}**
- **Summary:** Reject faculty (form submission from email link)
- **Authentication:** None
- **Path Parameter:** `id` (UUID of faculty)
- **Response:** HTML success page
- **Status Codes:** 200 OK, 400 Error
- **Frontend Status:** ✅ Implemented (HTML form in email)

#### 2.3 Faculty Admin Controller (`/api/admin`)

**GET /api/admin/pending-faculties**
- **Summary:** Get pending faculties awaiting approval
- **Authentication:** Required (ADMIN only)
- **Query Parameters:**
  - `page` (int, default=0)
  - `size` (int, default=20)
- **Response:** Paginated pending faculties
- **Status Codes:** 200 OK, 403 Forbidden
- **Frontend Status:** ⚠️ Needs verification (admin dashboard)

**POST /api/admin/approve-faculty/{id}**
- **Summary:** Approve pending faculty (from admin dashboard)
- **Authentication:** Required (ADMIN only)
- **Path Parameter:** `id` (UUID of faculty)
- **Response:** Success message
- **Status Codes:** 200 OK, 404 Not found, 403 Forbidden
- **Frontend Status:** ⚠️ Needs verification (admin approval action)

**POST /api/admin/reject-faculty/{id}**
- **Summary:** Reject pending faculty (from admin dashboard)
- **Authentication:** Required (ADMIN only)
- **Path Parameter:** `id` (UUID of faculty)
- **Response:** Success message
- **Status Codes:** 200 OK, 404 Not found, 403 Forbidden
- **Frontend Status:** ⚠️ Needs verification (admin rejection action)

#### 2.4 Faculty Delete Controller (`/api/admin/faculties`)

**DELETE /api/admin/faculties/{id}**
- **Summary:** Delete a faculty
- **Authentication:** Required (ADMIN only)
- **Path Parameter:** `id` (UUID of faculty)
- **Response:** Success message
- **Status Codes:** 200 OK, 404 Not found, 403 Forbidden
- **Frontend Status:** ❌ Missing (admin feature, unlikely in regular interface)

---

### Category 3: Academic - Students (7 endpoints)

#### 3.1 Student Controller (`/api/v1/students`)

**GET /api/v1/students/search**
- **Summary:** Search for students by email
- **Authentication:** Likely required (for advisor selection)
- **Query Parameters:** `email` (string, email to search)
- **Response:** List of matching student profiles
- **Status Codes:** 200 OK, 400 Invalid email
- **Frontend Status:** ⚠️ Needs verification (advisor finding students to invite)

#### 3.2 Student Admin Controller (`/api/admin/students`)

**GET /api/admin/students/pending**
- **Summary:** Get pending students awaiting profile approval
- **Authentication:** Required (ADMIN only)
- **Response:** List of students with unverified status
- **Status Codes:** 200 OK, 403 Forbidden
- **Frontend Status:** ⚠️ Needs verification (admin dashboard)

**POST /api/admin/students/{id}/approve**
- **Summary:** Approve student profile (from admin dashboard)
- **Authentication:** Required (ADMIN only)
- **Path Parameter:** `id` (UUID of student)
- **Response:** Success message
- **Status Codes:** 200 OK, 404 Not found, 403 Forbidden
- **Frontend Status:** ⚠️ Needs verification (admin approval action)

**POST /api/admin/students/{id}/reject**
- **Summary:** Reject student profile (from admin dashboard)
- **Authentication:** Required (ADMIN only)
- **Path Parameter:** `id` (UUID of student)
- **Response:** Success message
- **Status Codes:** 200 OK, 404 Not found, 403 Forbidden
- **Frontend Status:** ⚠️ Needs verification (admin rejection action)

#### 3.3 Student Approval Controller (`/api/faculty-action`)

**GET /api/faculty-action/approve-student**
- **Summary:** Display HTML confirmation page for student approval (email link)
- **Authentication:** None
- **Query Parameter:** `id` (UUID of student)
- **Response:** HTML confirmation page
- **Status Codes:** 200 OK, 404 Not found
- **Frontend Status:** ✅ Implemented (HTML email link)

**POST /api/faculty-action/approve-student**
- **Summary:** Approve student profile (email link submission)
- **Authentication:** None
- **Query Parameter:** `id` (UUID of student)
- **Response:** HTML success page
- **Status Codes:** 200 OK, 400 Error
- **Frontend Status:** ✅ Implemented (HTML form)

**GET /api/faculty-action/reject-student**
- **Summary:** Display HTML confirmation page for student rejection
- **Authentication:** None
- **Query Parameter:** `id` (UUID of student)
- **Response:** HTML confirmation page
- **Status Codes:** 200 OK, 404 Not found
- **Frontend Status:** ✅ Implemented (HTML email link)

**POST /api/faculty-action/reject-student**
- **Summary:** Reject student profile (email link submission)
- **Authentication:** None
- **Query Parameter:** `id` (UUID of student)
- **Response:** HTML success page
- **Status Codes:** 200 OK, 400 Error
- **Frontend Status:** ✅ Implemented (HTML form)

---

### Category 4: Academic - Advisors (6 endpoints)

#### 4.1 Advisor Controller (`/api/v1/advisors`)

**GET /api/v1/advisors/faculty/{facultyId}**
- **Summary:** Get all approved advisors for a specific faculty
- **Authentication:** None (public discovery)
- **Path Parameter:** `facultyId` (UUID of faculty)
- **Response:** List of advisor profiles
  ```json
  [
    {
      "id": "uuid",
      "userId": "uuid",
      "fullName": "Dr. Jane Smith",
      "email": "jane@faculty.edu",
      "facultyDomainEmail": "jane.smith@faculty.edu",
      "facultyId": "uuid",
      "status": "APPROVED"
    }
  ]
  ```
- **Status Codes:** 200 OK, 404 Faculty not found
- **Frontend Status:** ✅ Likely implemented (advisor list for project selection)

#### 4.2 Faculty Assignment Controller (`/api/faculty-assignments`)

**POST /api/faculty-assignments**
- **Summary:** Submit advisor assignment request (advisor to faculty admin)
- **Authentication:** Required (ADVISOR only)
- **Request:** `SubmitFacultyAssignmentCommand` body
  ```json
  {
    "advisorId": "uuid",
    "facultyId": "uuid",
    "message": "Requesting assignment to this faculty"
  }
  ```
- **Response:** Success message
- **Status Codes:** 201 Created, 400 Validation, 401 Unauthorized
- **Frontend Status:** ⚠️ Needs verification (advisor assignment form)

#### 4.3 Advisor Approval Controller (`/api/faculty-action`)

**GET /api/faculty-action/approve-advisor**
- **Summary:** Display HTML confirmation for advisor approval (email link)
- **Authentication:** None
- **Query Parameters:** `advisorId` (UUID), `facultyId` (UUID)
- **Response:** HTML confirmation page
- **Status Codes:** 200 OK
- **Frontend Status:** ✅ Implemented (HTML email link)

**POST /api/faculty-action/approve-advisor**
- **Summary:** Approve advisor assignment (email link submission)
- **Authentication:** None
- **Query Parameters:** `advisorId` (UUID), `facultyId` (UUID)
- **Response:** HTML success page
- **Status Codes:** 200 OK, 400 Error
- **Frontend Status:** ✅ Implemented (HTML form)

**GET /api/faculty-action/reject-advisor**
- **Summary:** Display HTML confirmation for advisor rejection
- **Authentication:** None
- **Query Parameters:** `advisorId` (UUID), `facultyId` (UUID)
- **Response:** HTML confirmation page
- **Status Codes:** 200 OK
- **Frontend Status:** ✅ Implemented (HTML email link)

**POST /api/faculty-action/reject-advisor**
- **Summary:** Reject advisor assignment (email link submission)
- **Authentication:** None
- **Query Parameters:** `advisorId` (UUID), `facultyId` (UUID)
- **Response:** HTML success page
- **Status Codes:** 200 OK, 400 Error
- **Frontend Status:** ✅ Implemented (HTML form)

---

### Category 5: Academic - Projects (9 endpoints)

#### 5.1 Project Controller (`/api/v1/projects`)

**POST /api/v1/projects**
- **Summary:** Create a new project (student team)
- **Authentication:** Required (STUDENT only)
- **Request:**
  ```json
  {
    "title": "PFE Project Title",
    "description": "Project description and objectives"
  }
  ```
- **Response:** Created project ID
- **Status Codes:** 201 Created, 400 Validation, 401 Unauthorized
- **Frontend Status:** ✅ Likely implemented (project creation page)

**GET /api/v1/projects/me**
- **Summary:** Get current student's project
- **Authentication:** Required (STUDENT only)
- **Response:** Current project details with team members
  ```json
  {
    "id": "uuid",
    "title": "PFE Project",
    "description": "Description",
    "ownerUserId": "uuid",
    "facultyId": "uuid",
    "status": "ACTIVE",
    "members": [
      { "userId": "uuid", "fullName": "John Doe", "role": "OWNER" },
      { "userId": "uuid", "fullName": "Jane Smith", "role": "MEMBER" }
    ]
  }
  ```
- **Status Codes:** 200 OK, 401 Unauthorized, 404 No project
- **Frontend Status:** ✅ Likely implemented (project dashboard)

**POST /api/v1/projects/invite**
- **Summary:** Invite a friend to the current project
- **Authentication:** Required (STUDENT, project owner)
- **Request:**
  ```json
  {
    "projectId": "uuid",
    "inviteeId": "uuid"
  }
  ```
- **Response:** Invitation created message
- **Status Codes:** 201 Created, 400 Validation, 401 Unauthorized, 403 Not project owner
- **Frontend Status:** ⚠️ Needs verification (friend invitation feature)

**GET /api/v1/projects/invitations/me**
- **Summary:** Get all pending project invitations for current student
- **Authentication:** Required (STUDENT only)
- **Response:** List of pending invitations
  ```json
  [
    {
      "id": "uuid",
      "projectId": "uuid",
      "projectTitle": "PFE Project",
      "ownerName": "John Doe",
      "status": "PENDING",
      "createdAt": "2026-01-01T00:00:00Z"
    }
  ]
  ```
- **Status Codes:** 200 OK, 401 Unauthorized
- **Frontend Status:** ⚠️ Needs verification (invitation notifications/list)

**POST /api/v1/projects/invitations/{id}/accept**
- **Summary:** Accept a project invitation
- **Authentication:** Required (STUDENT only)
- **Path Parameter:** `id` (UUID of invitation)
- **Response:** Success message
- **Status Codes:** 200 OK, 401 Unauthorized, 404 Invitation not found
- **Frontend Status:** ⚠️ Needs verification (accept invitation button)

**POST /api/v1/projects/invitations/{id}/reject**
- **Summary:** Reject a project invitation
- **Authentication:** Required (STUDENT only)
- **Path Parameter:** `id` (UUID of invitation)
- **Response:** Success message
- **Status Codes:** 200 OK, 401 Unauthorized, 404 Invitation not found
- **Frontend Status:** ⚠️ Needs verification (reject invitation button)

---

### Category 6: Academic - Advisor Selection (7 endpoints)

#### 6.1 Selection Request Controller (`/api/v1/selection-requests`)

**POST /api/v1/selection-requests**
- **Summary:** Submit a selection request (student/team to advisor)
- **Authentication:** Required (STUDENT only, project member)
- **Request:**
  ```json
  {
    "projectId": "uuid",
    "advisorProfileId": "uuid",
    "message": "We would like you as our advisor for this project"
  }
  ```
- **Response:** Created selection request ID
- **Status Codes:** 201 Created, 400 Validation, 401 Unauthorized
- **Frontend Status:** ✅ Likely implemented (advisor selection flow)

**GET /api/v1/selection-requests/student/{userId}**
- **Summary:** Get current selection for a student
- **Authentication:** Required
- **Path Parameter:** `userId` (UUID of student)
- **Response:** Current/pending selection request
- **Status Codes:** 200 OK, 404 No selection
- **Frontend Status:** ⚠️ Needs verification (current selection display)

**GET /api/v1/selection-requests/advisor/{advisorUserId}/faculty/{facultyId}**
- **Summary:** Get all student selection requests for an advisor in faculty
- **Authentication:** Required (ADVISOR only, or ADMIN)
- **Path Parameters:** 
  - `advisorUserId` (UUID of advisor)
  - `facultyId` (UUID of faculty)
- **Response:** List of pending selection requests
  ```json
  [
    {
      "id": "uuid",
      "projectId": "uuid",
      "projectTitle": "PFE Project",
      "studentNames": ["John Doe", "Jane Smith"],
      "message": "Request message",
      "status": "PENDING",
      "submittedAt": "2026-01-01T00:00:00Z"
    }
  ]
  ```
- **Status Codes:** 200 OK, 401 Unauthorized
- **Frontend Status:** ⚠️ Needs verification (advisor dashboard - pending selections)

**PUT /api/v1/selection-requests/{id}/approve**
- **Summary:** Approve a selection request (advisor action)
- **Authentication:** Required (ADVISOR only)
- **Path Parameter:** `id` (UUID of selection request)
- **Response:** Approval success message
- **Status Codes:** 200 OK, 401 Unauthorized, 404 Not found
- **Frontend Status:** ⚠️ Needs verification (approve button in advisor dashboard)

**PUT /api/v1/selection-requests/{id}/reject**
- **Summary:** Reject a selection request (advisor action)
- **Authentication:** Required (ADVISOR only)
- **Path Parameter:** `id` (UUID of selection request)
- **Response:** Rejection success message
- **Status Codes:** 200 OK, 401 Unauthorized, 404 Not found
- **Frontend Status:** ⚠️ Needs verification (reject button in advisor dashboard)

#### 6.2 Selection Approval Controller (`/api/faculty-action`)

**GET /api/faculty-action/approve-selection**
- **Summary:** Display HTML confirmation for selection approval (email link)
- **Authentication:** None
- **Query Parameter:** `id` (UUID of selection request)
- **Response:** HTML confirmation page
- **Status Codes:** 200 OK, 404 Not found
- **Frontend Status:** ✅ Implemented (HTML email link)

**POST /api/faculty-action/approve-selection**
- **Summary:** Approve selection request (email link submission)
- **Authentication:** None
- **Query Parameter:** `id` (UUID of selection request)
- **Response:** HTML success page
- **Status Codes:** 200 OK, 400 Error
- **Frontend Status:** ✅ Implemented (HTML form)

**GET /api/faculty-action/reject-selection**
- **Summary:** Display HTML confirmation for selection rejection
- **Authentication:** None
- **Query Parameter:** `id` (UUID of selection request)
- **Response:** HTML confirmation page
- **Status Codes:** 200 OK, 404 Not found
- **Frontend Status:** ✅ Implemented (HTML email link)

**POST /api/faculty-action/reject-selection**
- **Summary:** Reject selection request (email link submission)
- **Authentication:** None
- **Query Parameter:** `id` (UUID of selection request)
- **Response:** HTML success page
- **Status Codes:** 200 OK, 400 Error
- **Frontend Status:** ✅ Implemented (HTML form)

---

### Category 7: File Management (2 endpoints)

#### 7.1 Upload Controller (`/api/v1/uploads`)

**POST /api/v1/uploads/draft**
- **Summary:** Upload file to Cloudinary (drafts for card/cin)
- **Authentication:** Required (JWT, any authenticated user)
- **Request:** Multipart form data
  - `file`: File (PDF, image, max 5MB for images, 50MB for video)
- **Validation:** 
  - Allowed types: image/jpeg, image/png, image/webp, video/mp4, application/pdf
  - Max size: 5MB (images), 50MB (videos)
  - Filename: alphanumeric, dash, underscore, dot only
- **Response:** Cloudinary draft object
  ```json
  {
    "publicId": "cloudinary-public-id",
    "secureUrl": "https://cloudinary.url/file.jpg",
    "size": 102400,
    "format": "jpg"
  }
  ```
- **Status Codes:** 200 OK, 400 File validation failed, 413 File too large
- **Frontend Status:** ✅ Likely implemented (file upload in signup forms)

**DELETE /api/v1/uploads/draft/{publicId}**
- **Summary:** Delete uploaded draft file from Cloudinary
- **Authentication:** Required (JWT, owner only)
- **Path Parameter:** `publicId` (Cloudinary public ID)
- **Response:** Success message
- **Status Codes:** 200 OK, 400 Invalid ID, 401 Unauthorized, 404 Not found
- **Frontend Status:** ⚠️ Needs verification (delete file capability)

---

### Category 8: Seasons & Admin (3 endpoints)

#### 8.1 Season Controller (`/api/admin/seasons`)

**POST /api/admin/seasons**
- **Summary:** Create a new season (PFE cycle)
- **Authentication:** Required (ADMIN only)
- **Request:**
  ```json
  {
    "name": "2025-2026",
    "startDate": "2025-09-01",
    "endDate": "2026-06-30"
  }
  ```
- **Response:** Created season ID
- **Status Codes:** 201 Created, 400 Validation, 403 Forbidden
- **Frontend Status:** ❌ Missing (admin-only feature)

**GET /api/admin/seasons/active**
- **Summary:** Get the currently active season
- **Authentication:** None (public)
- **Response:** Active season details
  ```json
  {
    "id": "uuid",
    "name": "2025-2026",
    "startDate": "2025-09-01",
    "endDate": "2026-06-30",
    "isActive": true
  }
  ```
- **Status Codes:** 200 OK, 404 No active season
- **Frontend Status:** ⚠️ Needs verification (used in application logic - timeline display)

**PUT /api/admin/seasons/{id}/activate**
- **Summary:** Activate a specific season
- **Authentication:** Required (ADMIN only)
- **Path Parameter:** `id` (UUID of season)
- **Response:** Success message
- **Status Codes:** 200 OK, 404 Not found, 403 Forbidden
- **Frontend Status:** ❌ Missing (admin-only feature)

#### 8.2 Admin User Controller (`/api/admin`)

**GET /api/admin/users**
- **Summary:** Get all pending users (students by default)
- **Authentication:** Required (ADMIN only)
- **Response:** List of pending users
- **Status Codes:** 200 OK, 403 Forbidden
- **Frontend Status:** ⚠️ Needs verification (admin user management)

---

### Category 9: Notifications (4 endpoints)

#### 9.1 Notification Controller (`/api/notifications`)

**GET /api/notifications/me**
- **Summary:** Get current user's notifications with pagination
- **Authentication:** Required (JWT)
- **Query Parameters:**
  - `page` (int, default=0)
  - `limit` (int, default=20)
- **Response:** Paginated notifications
  ```json
  {
    "content": [
      {
        "id": "uuid",
        "userId": "uuid",
        "title": "Your selection was approved",
        "message": "Advisor has approved your selection request",
        "type": "SELECTION_APPROVED",
        "read": false,
        "createdAt": "2026-01-01T00:00:00Z"
      }
    ],
    "totalElements": 50,
    "currentPage": 0
  }
  ```
- **Status Codes:** 200 OK, 401 Unauthorized
- **Frontend Status:** ⚠️ Needs verification (notifications panel/page)

**GET /api/notifications/me/unread-count**
- **Summary:** Get count of unread notifications
- **Authentication:** Required (JWT)
- **Response:** Unread count
  ```json
  {
    "count": 5
  }
  ```
- **Status Codes:** 200 OK, 401 Unauthorized
- **Frontend Status:** ⚠️ Needs verification (unread badge display)

**PATCH /api/notifications/me/read/{notificationId}**
- **Summary:** Mark a specific notification as read
- **Authentication:** Required (JWT)
- **Path Parameter:** `notificationId` (UUID)
- **Response:** Success message
- **Status Codes:** 200 OK, 401 Unauthorized, 404 Not found
- **Frontend Status:** ⚠️ Needs verification (mark as read action)

**PATCH /api/notifications/me/read-all**
- **Summary:** Mark all notifications as read
- **Authentication:** Required (JWT)
- **Response:** Success message
- **Status Codes:** 200 OK, 401 Unauthorized
- **Frontend Status:** ⚠️ Needs verification (mark all as read action)

---

## Backend Implementation Change Required

### Unified GET /api/auth/me Endpoint Enhancement

**Current State:**
- GET /api/auth/me returns base User entity only
- GET /api/v1/students/me returns student-specific data
- Role-specific endpoints scattered across controllers

**Required Changes:**

**Step 1: Update GetMeQuery & GetMeQueryHandler**
- Enhance to load complete user data including role-specific information
- Modify response DTO to include role-specific nested objects

**Step 2: Create Enhanced MeResponse DTO**
```java
public record MeResponse(
    UUID id,
    String email,
    String fullName,
    String telephone,
    UserRole role,
    LocalDateTime createdAt,
    StudentProfileData studentProfile,    // null if not student
    AdvisorProfileData advisorProfile,    // null if not advisor
    FacultyProfileData facultyProfile     // null if not faculty
) {}

public record StudentProfileData(
    UUID facultyId,
    String studentCardUrl,
    StudentStatus status,
    VerificationStatus verificationStatus
) {}

public record AdvisorProfileData(
    UUID facultyId,
    String facultyDomainEmail,
    AdvisorStatus assignmentStatus
) {}

public record FacultyProfileData(
    String facultyName,
    String abbreviation,
    FacultyStatus adminStatus
) {}
```

**Step 3: Remove GET /api/v1/students/me**
- Delete StudentController endpoint
- Update frontend to use GET /api/auth/me instead
- Ensure all role-specific data is now in the unified endpoint

**Step 4: Update GetMeQueryHandler Logic**
```java
@Component
public class GetMeQueryHandler implements IQueryHandler<GetMeQuery, MeResponse> {
    @Override
    public MeResponse handle(GetMeQuery query) {
        User user = userRepository.findById(query.userId()).orElseThrow();
        
        StudentProfileData student = null;
        if (user.getRole() == UserRole.STUDENT) {
            Student studentProfile = studentRepository.findByUserId(user.getId()).orElse(null);
            student = mapToStudentData(studentProfile);
        }
        
        AdvisorProfileData advisor = null;
        if (user.getRole() == UserRole.ADVISOR) {
            Advisor advisorProfile = advisorRepository.findByUserId(user.getId()).orElse(null);
            advisor = mapToAdvisorData(advisorProfile);
        }
        
        FacultyProfileData faculty = null;
        if (user.getRole() == UserRole.FACULTY_ADMIN) {
            Faculty facultyProfile = facultyRepository.findByAdminUserId(user.getId()).orElse(null);
            faculty = mapToFacultyData(facultyProfile);
        }
        
        return new MeResponse(
            user.getId(),
            user.getEmail(),
            user.getFullName(),
            user.getTelephone(),
            user.getRole(),
            user.getCreatedAt(),
            student,
            advisor,
            faculty
        );
    }
}
```

**Frontend Impact:**
- Single endpoint for all user types
- Cleaner API design
- Easier to cache complete user profile
- Eliminates 401 on /api/v1/students/me for non-students

---

## Frontend Implementation Gap Analysis

### Summary Statistics
- **Total Endpoints:** 59 (unified /api/auth/me for all roles)
- **Likely Implemented:** 13 (22%) ✅ (includes unified GET /api/auth/me)
- **Needs Verification:** 34 (58%) ⚠️
- **Missing/Not Implemented:** 12 (20%) ❌

### High Priority Gaps (Must Implement)

#### 1. Profile Management (3 endpoints)
- **PUT /api/profile/student** - Student profile edit form
- **PUT /api/profile/advisor** - Advisor profile edit form
- **POST /api/profile/change-password** - Password change form
- **Impact:** Core user features, medium complexity
- **Estimated Effort:** 2-3 tasks

#### 2. Password Recovery Flow (3 endpoints)
- **POST /api/auth/verify-otp** - OTP verification form
- **POST /api/auth/reset-password** - Password reset form
- **POST /api/auth/forgot-password** - Forgot password form
- **Impact:** User support feature, medium complexity
- **Estimated Effort:** 2-3 tasks

#### 3. Project Team Management (4 endpoints)
- **POST /api/v1/projects/invite** - Team member invitation UI
- **GET /api/v1/projects/invitations/me** - Pending invitations display
- **POST /api/v1/projects/invitations/{id}/accept** - Accept invitation action
- **POST /api/v1/projects/invitations/{id}/reject** - Reject invitation action
- **Impact:** Core PFE workflow, high complexity
- **Estimated Effort:** 3-4 tasks

#### 4. Advisor Dashboard - Selection Requests (3 endpoints)
- **GET /api/v1/selection-requests/advisor/{advisorUserId}/faculty/{facultyId}** - List pending selections
- **PUT /api/v1/selection-requests/{id}/approve** - Approve button
- **PUT /api/v1/selection-requests/{id}/reject** - Reject button
- **Impact:** Critical advisor workflow, high complexity
- **Estimated Effort:** 3-4 tasks

#### 5. Notifications System (4 endpoints)
- **GET /api/notifications/me** - Notifications list/panel
- **GET /api/notifications/me/unread-count** - Unread badge
- **PATCH /api/notifications/me/read/{notificationId}** - Mark read action
- **PATCH /api/notifications/me/read-all** - Mark all read action
- **Impact:** User engagement feature, medium complexity
- **Estimated Effort:** 2-3 tasks

#### 6. Student/Advisor Form Fields (2 endpoints)
- **GET /api/v1/students/search** - Student search for advisor selection
- **GET /api/v1/advisors/faculty/{facultyId}** - Advisor list (may exist)
- **Impact:** Selection workflow, low-medium complexity
- **Estimated Effort:** 1-2 tasks

### Medium Priority Gaps

#### 7. File Upload/Delete (2 endpoints)
- **DELETE /api/v1/uploads/draft/{publicId}** - Delete uploaded file
- **POST /api/v1/uploads/draft** - File upload (may exist)
- **Impact:** User convenience, low complexity
- **Estimated Effort:** 1 task

#### 8. Admin Features (5 endpoints)
- **GET /api/admin/pending-faculties** - Faculty approval list
- **POST /api/admin/approve-faculty/{id}** - Faculty approve
- **POST /api/admin/reject-faculty/{id}** - Faculty reject
- **POST /api/admin/students/{id}/approve** - Student approve
- **POST /api/admin/students/{id}/reject** - Student reject
- **Impact:** Admin-only, can be deferred
- **Estimated Effort:** 2-3 tasks

#### 9. Faculty/Advisor Assignment (1 endpoint)
- **POST /api/faculty-assignments** - Advisor submit assignment request
- **Impact:** Limited users (advisors), low-medium priority
- **Estimated Effort:** 1 task

### Low Priority Gaps (Admin/Rare)

#### 10. Season Management (3 endpoints)
- **POST /api/admin/seasons** - Create season (admin only)
- **PUT /api/admin/seasons/{id}/activate** - Activate season (admin only)
- **GET /api/admin/seasons/active** - Display active season (may exist)
- **Impact:** Admin-only, no regular user impact
- **Estimated Effort:** 1-2 tasks

#### 11. Faculty Creation (1 endpoint)
- **POST /api/v1/faculties** - Create faculty (admin only, rare)
- **Impact:** Minimal, admin-only feature
- **Estimated Effort:** 1 task

#### 12. Faculty Delete (1 endpoint)
- **DELETE /api/admin/faculties/{id}** - Delete faculty (destructive)
- **Impact:** Minimal, dangerous operation
- **Estimated Effort:** 1 task

#### 13. Admin Users List (1 endpoint)
- **GET /api/admin/users** - List all users
- **Impact:** Admin dashboard, low priority
- **Estimated Effort:** 1 task

---

## Implementation Priority Roadmap

### Phase 1: Core User Features (High Priority - Weeks 1-2)
1. **Password Recovery Flow** (3 endpoints)
   - Forgot password form → OTP email verification → Reset password form
   
2. **Profile Management** (3 endpoints)
   - Student & Advisor profile edit forms
   - Change password form

3. **Student Search** (1 endpoint)
   - Search functionality for advisor selection

**Expected Impact:** Complete authentication & user profile workflow

### Phase 2: Core PFE Workflow (High Priority - Weeks 2-3)
4. **Project Team Management** (4 endpoints)
   - Team member invitations
   - Accept/reject invitations
   - Invitation notifications

5. **Advisor Selection Dashboard** (3 endpoints)
   - Advisor pending requests list
   - Approve/reject buttons
   - Request details display

**Expected Impact:** Complete team formation & advisor selection flow

### Phase 3: User Experience (Medium Priority - Week 4)
6. **Notifications System** (4 endpoints)
   - Notifications panel/page
   - Unread badge/counter
   - Mark as read actions

7. **File Management** (1 endpoint)
   - Delete uploaded files

**Expected Impact:** Better user engagement & feedback

### Phase 4: Admin Features (Low Priority - Ongoing)
8. **Admin Dashboards** (5+ endpoints)
   - Faculty/student approval management
   - Season management
   - User management

**Expected Impact:** Administrative capabilities

---

## Testing Strategy

### Endpoints Requiring Frontend Testing
1. **Authentication Flow Tests**
   - Login → Get token → Refresh token → Logout
   - Forgot password → OTP → Reset password

2. **CRUD Operations**
   - Create project → Update profile → Delete file
   - Accept/reject invitations

3. **Complex Workflows**
   - Student signup → Faculty selection → Advisor assignment
   - Team formation → Selection → Advisor approval

4. **Error Handling**
   - Network timeouts
   - Validation errors (reCAPTCHA, OTP expiry)
   - Authorization failures (403, 401)
   - File size limits

---

## Common Response Envelope

All API responses follow this standardized format:

```json
{
  "success": true/false,
  "data": { /* actual response data */ },
  "error": {
    "code": "ERROR_CODE",
    "message": "Human readable error message"
  },
  "timestamp": "2026-01-01T00:00:00Z"
}
```

### Status Codes Guide
- **200 OK** - Success
- **201 Created** - Resource created
- **400 Bad Request** - Validation error
- **401 Unauthorized** - Missing/invalid JWT
- **403 Forbidden** - Insufficient permissions
- **404 Not Found** - Resource not found
- **409 Conflict** - Duplicate resource (e.g., email exists)
- **413 Payload Too Large** - File exceeds size limit
- **422 Unprocessable Entity** - Validation/business logic error
- **500 Internal Server Error** - Server error

---

## API Documentation Reference

**Live Swagger UI:** http://localhost:8081/swagger-ui.html
**OpenAPI JSON:** http://localhost:8081/api-docs

Endpoints are organized by controller tags in Swagger for easy browsing.

---

**Last Updated:** May 17, 2026  
**Total Endpoints Documented:** 59 (unified GET /api/auth/me for all roles)  
**Controllers:** 13 (Auth, Faculty, Student, Advisor, Project, Selection, Upload, Season, Notifications, Admin)  
**Key Design:** Single GET /api/auth/me endpoint returns role-specific data for all user types
