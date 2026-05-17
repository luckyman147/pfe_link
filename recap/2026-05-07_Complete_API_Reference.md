# Heysir API Reference & Frontend-Backend Endpoint Mapping
**Date:** May 7, 2026  
**Status:** Complete Backend Endpoints | Frontend Integration Audit

---

## ⚠️ CRITICAL ISSUES FOUND

### Frontend API Calls vs Backend Endpoints Mismatches

| Issue | Frontend Call | Backend Endpoint | Status |
|-------|---------------|------------------|--------|
| **1. Profile Endpoints Missing** | `PUT /api/profile/student` | ❌ NOT IMPLEMENTED | ⚠️ FIX NEEDED |
| **2. Admin Student Endpoints** | `GET /api/admin/students/pending` | ❌ NOT IMPLEMENTED | ⚠️ FIX NEEDED |
| **3. Admin Approval Endpoints** | `POST /api/admin/students/{id}/approve` | ❌ NOT IMPLEMENTED | ⚠️ FIX NEEDED |
| **4. API Version Inconsistency** | `/api/auth/...` | ✅ `/api/auth/...` | ✅ CORRECT |
| **5. Projects Endpoint** | `/api/projects/...` | ✅ `/api/v1/projects/...` | ⚠️ NEEDS UPDATE |
| **6. Faculty Endpoint** | `/api/faculties/...` | ✅ `/api/v1/faculties/...` | ⚠️ NEEDS UPDATE |
| **7. Students Endpoint** | `/api/students/...` | ✅ `/api/v1/students/...` | ⚠️ NEEDS UPDATE |
| **8. Advisors Endpoint** | `/api/advisors/...` | ✅ `/api/v1/advisors/...` | ⚠️ NEEDS UPDATE |
| **9. Upload Endpoint** | `/api/uploads/...` | ✅ `/api/v1/uploads/...` | ⚠️ NEEDS UPDATE |

---

## 🔐 AUTHENTICATION ENDPOINTS

### 1. Login
**Path:** `POST /api/auth/login`  
**Auth Required:** ❌ No  
**Status:** ✅ Implemented

**Request:**
```json
{
  "email": "user@example.com",
  "password": "SecurePassword123!",
  "recaptchaToken": "token_from_google_recaptcha"
}
```

**Response (Success):**
```json
{
  "success": true,
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "email": "user@example.com",
    "fullName": "John Doe",
    "role": "STUDENT",
    "status": "ACTIVE",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

**Response (Failure):**
```json
{
  "success": false,
  "error": {
    "code": "Auth.InvalidCredentials",
    "message": "Invalid email or password"
  }
}
```

---

### 2. Get Current User (Logged In)
**Path:** `GET /api/auth/me`  
**Auth Required:** ✅ Yes (Bearer Token)  
**Status:** ✅ Implemented

**Request:**
```
No body required
Headers: Authorization: Bearer {access_token}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "email": "user@example.com",
    "fullName": "John Doe",
    "role": "STUDENT",
    "status": "ACTIVE"
  }
}
```

---

### 3. Logout
**Path:** `POST /api/auth/logout`  
**Auth Required:** ✅ Yes (Bearer Token)  
**Status:** ✅ Implemented

**Request:**
```
No body required
Headers: Authorization: Bearer {access_token}
```

**Response:**
```json
{
  "message": "Logged out successfully"
}
```

---

### 4. Refresh Token
**Path:** `POST /api/auth/refresh`  
**Auth Required:** ❌ No (Token in Cookie or Body)  
**Status:** ✅ Implemented

**Request Option 1 (Cookie-based):**
```
Cookie: refresh_token={refresh_token}
```

**Request Option 2 (Body-based):**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "email": "user@example.com",
    "fullName": "John Doe",
    "role": "STUDENT",
    "status": "ACTIVE",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

---

## 📝 REGISTRATION ENDPOINTS

### 1. Register Student
**Path:** `POST /api/auth/signup/student`  
**Auth Required:** ❌ No  
**Status:** ✅ Implemented

**Request:**
```json
{
  "email": "student@example.com",
  "password": "SecurePassword123!",
  "fullName": "Ahmed Hassan",
  "telephone": "+212612345678",
  "cinNumber": "AB123456",
  "studentCardUrl": "https://cloudinary.example.com/draft/student_card.jpg",
  "draftId": "draft_id_123",
  "facultyId": "550e8400-e29b-41d4-a716-446655440000",
  "recaptchaToken": "token_from_google_recaptcha"
}
```

**Response (Success):**
```json
{
  "success": true,
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "email": "student@example.com",
    "fullName": "Ahmed Hassan",
    "role": "STUDENT",
    "status": "PENDING",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

---

### 2. Register Advisor
**Path:** `POST /api/auth/signup/advisor`  
**Auth Required:** ❌ No  
**Status:** ✅ Implemented

**Request:**
```json
{
  "email": "advisor@example.com",
  "password": "SecurePassword123!",
  "fullName": "Dr. Maria Garcia",
  "telephone": "+212698765432",
  "cinNumber": "CD789012",
  "cinCardUrl": "https://cloudinary.example.com/draft/cin_card.jpg",
  "draftId": "draft_id_456",
  "facultyId": "550e8400-e29b-41d4-a716-446655440000",
  "facultyDomainEmail": "maria@faculty.edu.ma",
  "recaptchaToken": "token_from_google_recaptcha"
}
```

**Response (Success):**
```json
{
  "success": true,
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440002",
    "email": "advisor@example.com",
    "fullName": "Dr. Maria Garcia",
    "role": "ADVISOR",
    "status": "PENDING",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

---

### 3. Verify Email
**Path:** `POST /api/auth/verify-email`  
**Auth Required:** ❌ No  
**Status:** ✅ Implemented

**Request:**
```
Query Params: ?token={email_verification_token}
```

**Response:**
```json
{
  "success": true,
  "data": "Email verified successfully"
}
```

---

## 🔑 PASSWORD RECOVERY ENDPOINTS

### 1. Forgot Password (Request OTP)
**Path:** `POST /api/auth/forgot-password`  
**Auth Required:** ❌ No  
**Status:** ✅ Implemented

**Request:**
```json
{
  "email": "user@example.com"
}
```

**Response:**
```json
{
  "success": true,
  "data": "OTP sent to your email"
}
```

---

### 2. Verify OTP
**Path:** `POST /api/auth/verify-otp`  
**Auth Required:** ❌ No  
**Status:** ✅ Implemented

**Request:**
```json
{
  "email": "user@example.com",
  "otpCode": "123456"
}
```

**Response:**
```json
{
  "success": true,
  "data": "OTP verified successfully"
}
```

---

### 3. Reset Password
**Path:** `POST /api/auth/reset-password`  
**Auth Required:** ❌ No  
**Status:** ✅ Implemented

**Request:**
```json
{
  "email": "user@example.com",
  "otpCode": "123456",
  "newPassword": "NewSecurePassword123!"
}
```

**Response:**
```json
{
  "success": true,
  "data": "Password reset successfully"
}
```

---

## 👤 PROFILE ENDPOINTS

⚠️ **STATUS:** ❌ NOT IMPLEMENTED IN BACKEND

**These endpoints are called by frontend but don't exist in backend:**

### 1. Update Student Profile
**Path:** `PUT /api/profile/student`  
**Auth Required:** ✅ Yes  
**Status:** ❌ NOT IMPLEMENTED

**Frontend expects:**
```json
{
  "fullName": "Ahmed Hassan",
  "telephone": "+212612345678",
  "bio": "Passionate about software development",
  "specialization": "Web Development"
}
```

---

### 2. Update Advisor Profile
**Path:** `PUT /api/profile/advisor`  
**Auth Required:** ✅ Yes  
**Status:** ❌ NOT IMPLEMENTED

**Frontend expects:**
```json
{
  "fullName": "Dr. Maria Garcia",
  "telephone": "+212698765432",
  "bio": "Expert in AI and Machine Learning",
  "office": "Room 305, Faculty Building"
}
```

---

### 3. Change Password
**Path:** `POST /api/profile/change-password`  
**Auth Required:** ✅ Yes  
**Status:** ❌ NOT IMPLEMENTED

**Frontend expects:**
```json
{
  "currentPassword": "OldPassword123!",
  "newPassword": "NewPassword123!",
  "confirmPassword": "NewPassword123!"
}
```

---

## 🏛️ FACULTY ENDPOINTS

### 1. Get All Faculties
**Path:** `GET /api/v1/faculties`  
**Auth Required:** ❌ No  
**Status:** ✅ Implemented  
**Frontend Status:** ⚠️ Needs to update path from `/api/faculties`

**Request:**
```
Query Params: ?page=0&size=20
```

**Response:**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "name": "Faculty of Science",
        "abbreviation": "FS",
        "email": "science@university.edu.ma",
        "websiteUrl": "https://science.university.edu.ma",
        "imageUrl": "https://cloudinary.example.com/faculty/science.jpg",
        "status": "APPROVED"
      }
    ],
    "totalElements": 15,
    "totalPages": 1,
    "currentPage": 0
  }
}
```

---

### 2. Create Faculty
**Path:** `POST /api/v1/faculties`  
**Auth Required:** ✅ Yes (Admin)  
**Status:** ✅ Implemented  
**Frontend Status:** ⚠️ Needs to update path from `/api/faculties`

**Request:**
```json
{
  "name": "Faculty of Engineering",
  "abbreviation": "FE",
  "email": "engineering@university.edu.ma",
  "websiteUrl": "https://engineering.university.edu.ma",
  "imageUrl": "https://cloudinary.example.com/faculty/engineering.jpg",
  "adminPassword": "AdminPassword123!",
  "path": "/engineering"
}
```

**Response:**
```json
{
  "success": true,
  "data": "550e8400-e29b-41d4-a716-446655440003"
}
```

---

## 📚 STUDENT ENDPOINTS

### 1. Get My Student Profile
**Path:** `GET /api/v1/students/me`  
**Auth Required:** ✅ Yes (Student)  
**Status:** ✅ Implemented  
**Frontend Status:** ⚠️ Needs to update path from `/api/students`

**Request:**
```
No body required
Headers: Authorization: Bearer {access_token}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "email": "student@example.com",
    "fullName": "Ahmed Hassan",
    "studentNumber": "2024-001",
    "specialization": "Web Development",
    "facultyId": "550e8400-e29b-41d4-a716-446655440000",
    "facultyName": "Faculty of Science",
    "status": "ACTIVE",
    "createdAt": "2026-05-01T10:30:00Z"
  }
}
```

---

### 2. Search Students
**Path:** `GET /api/v1/students/search`  
**Auth Required:** ✅ Yes  
**Status:** ✅ Implemented  
**Frontend Status:** ⚠️ Needs to update path

**Request:**
```
Query Params: ?email=student@example.com
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440001",
      "email": "student@example.com",
      "fullName": "Ahmed Hassan",
      "studentNumber": "2024-001"
    }
  ]
}
```

---

## 👨‍🏫 ADVISOR ENDPOINTS

### 1. Get Advisors by Faculty
**Path:** `GET /api/v1/advisors/faculty/{facultyId}`  
**Auth Required:** ❌ No  
**Status:** ✅ Implemented  
**Frontend Status:** ⚠️ Needs to update path from `/api/advisors`

**Request:**
```
Path Param: facultyId = 550e8400-e29b-41d4-a716-446655440000
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440002",
      "email": "advisor@example.com",
      "fullName": "Dr. Maria Garcia",
      "facultyId": "550e8400-e29b-41d4-a716-446655440000",
      "facultyName": "Faculty of Science",
      "bio": "Expert in AI and Machine Learning",
      "office": "Room 305",
      "status": "APPROVED"
    }
  ]
}
```

---

## 📦 PROJECT ENDPOINTS

### 1. Create Project
**Path:** `POST /api/v1/projects`  
**Auth Required:** ✅ Yes (Student)  
**Status:** ✅ Implemented  
**Frontend Status:** ⚠️ Needs to update path from `/api/projects`

**Request:**
```json
{
  "title": "AI-Based Recommendation System",
  "description": "Building a machine learning system to recommend courses for students"
}
```

**Response:**
```json
{
  "success": true,
  "data": "550e8400-e29b-41d4-a716-446655440010"
}
```

---

### 2. Invite Friend to Project
**Path:** `POST /api/v1/projects/invite`  
**Auth Required:** ✅ Yes (Project Owner)  
**Status:** ✅ Implemented  
**Frontend Status:** ⚠️ Needs to update path

**Request:**
```json
{
  "projectId": "550e8400-e29b-41d4-a716-446655440010",
  "inviteeId": "550e8400-e29b-41d4-a716-446655440011"
}
```

**Response:**
```json
{
  "success": true,
  "data": "Invitation sent successfully"
}
```

---

### 3. Get My Pending Invitations
**Path:** `GET /api/v1/projects/invitations/me`  
**Auth Required:** ✅ Yes (Student)  
**Status:** ✅ Implemented  
**Frontend Status:** ⚠️ Needs to update path

**Request:**
```
No body required
Headers: Authorization: Bearer {access_token}
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440020",
      "projectId": "550e8400-e29b-41d4-a716-446655440010",
      "projectTitle": "AI-Based Recommendation System",
      "ownerFullName": "Ahmed Hassan",
      "ownerEmail": "student@example.com",
      "status": "PENDING",
      "createdAt": "2026-05-05T14:20:00Z"
    }
  ]
}
```

---

### 4. Accept Project Invitation
**Path:** `POST /api/v1/projects/invitations/{id}/accept`  
**Auth Required:** ✅ Yes (Invited Student)  
**Status:** ✅ Implemented  
**Frontend Status:** ⚠️ Needs to update path

**Request:**
```
Path Param: id = 550e8400-e29b-41d4-a716-446655440020
```

**Response:**
```json
{
  "success": true,
  "data": "Invitation accepted"
}
```

---

### 5. Reject Project Invitation
**Path:** `POST /api/v1/projects/invitations/{id}/reject`  
**Auth Required:** ✅ Yes (Invited Student)  
**Status:** ✅ Implemented  
**Frontend Status:** ⚠️ Needs to update path

**Request:**
```
Path Param: id = 550e8400-e29b-41d4-a716-446655440020
```

**Response:**
```json
{
  "success": true,
  "data": "Invitation rejected"
}
```

---

### 6. Get My Project
**Path:** `GET /api/v1/projects/me`  
**Auth Required:** ✅ Yes (Student)  
**Status:** ✅ Implemented  
**Frontend Status:** ⚠️ Needs to update path

**Request:**
```
Headers: Authorization: Bearer {access_token}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440010",
    "title": "AI-Based Recommendation System",
    "description": "Building a machine learning system...",
    "ownerId": "550e8400-e29b-41d4-a716-446655440001",
    "ownerName": "Ahmed Hassan",
    "members": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440001",
        "name": "Ahmed Hassan",
        "email": "student@example.com"
      },
      {
        "id": "550e8400-e29b-41d4-a716-446655440011",
        "name": "Fatima Ahmed",
        "email": "fatima@example.com"
      }
    ],
    "status": "ACTIVE",
    "createdAt": "2026-05-01T10:30:00Z"
  }
}
```

---

## 🎯 ADVISOR SELECTION ENDPOINTS

### 1. Submit Selection Request
**Path:** `POST /api/v1/selection-requests`  
**Auth Required:** ✅ Yes (Student)  
**Status:** ✅ Implemented

**Request:**
```json
{
  "projectId": "550e8400-e29b-41d4-a716-446655440010",
  "advisorProfileId": "550e8400-e29b-41d4-a716-446655440002",
  "message": "We believe your expertise in AI would be perfect for our project"
}
```

**Response:**
```json
{
  "success": true,
  "data": "Selection request submitted successfully"
}
```

---

### 2. Get Student's Current Selection
**Path:** `GET /api/v1/selection-requests/student/{userId}`  
**Auth Required:** ✅ Yes  
**Status:** ✅ Implemented

**Request:**
```
Path Param: userId = 550e8400-e29b-41d4-a716-446655440001
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440030",
    "projectId": "550e8400-e29b-41d4-a716-446655440010",
    "studentId": "550e8400-e29b-41d4-a716-446655440001",
    "advisorId": "550e8400-e29b-41d4-a716-446655440002",
    "advisorName": "Dr. Maria Garcia",
    "message": "We believe your expertise in AI...",
    "status": "PENDING",
    "createdAt": "2026-05-05T14:20:00Z"
  }
}
```

---

### 3. Get Advisor's Students
**Path:** `GET /api/v1/selection-requests/advisor/{advisorUserId}/faculty/{facultyId}`  
**Auth Required:** ✅ Yes (Advisor)  
**Status:** ✅ Implemented

**Request:**
```
Path Params: 
  - advisorUserId = 550e8400-e29b-41d4-a716-446655440002
  - facultyId = 550e8400-e29b-41d4-a716-446655440000
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440030",
      "projectId": "550e8400-e29b-41d4-a716-446655440010",
      "projectTitle": "AI-Based Recommendation System",
      "studentTeam": [
        {
          "id": "550e8400-e29b-41d4-a716-446655440001",
          "name": "Ahmed Hassan",
          "email": "student@example.com"
        }
      ],
      "message": "We believe your expertise...",
      "status": "PENDING",
      "createdAt": "2026-05-05T14:20:00Z"
    }
  ]
}
```

---

### 4. Approve Selection Request
**Path:** `PUT /api/v1/selection-requests/{id}/approve`  
**Auth Required:** ✅ Yes (Advisor)  
**Status:** ✅ Implemented

**Request:**
```
Path Param: id = 550e8400-e29b-41d4-a716-446655440030
```

**Response:**
```json
{
  "success": true,
  "data": "Selection request approved"
}
```

---

### 5. Reject Selection Request
**Path:** `PUT /api/v1/selection-requests/{id}/reject`  
**Auth Required:** ✅ Yes (Advisor)  
**Status:** ✅ Implemented

**Request:**
```
Path Param: id = 550e8400-e29b-41d4-a716-446655440030
```

**Response:**
```json
{
  "success": true,
  "data": "Selection request rejected"
}
```

---

## 📤 FILE UPLOAD ENDPOINTS

### 1. Upload Draft File
**Path:** `POST /api/v1/uploads/draft`  
**Auth Required:** ✅ Yes  
**Status:** ✅ Implemented  
**Frontend Status:** ⚠️ Needs to update path from `/api/uploads`

**Request (multipart/form-data):**
```
Form Data:
  - file: <binary file content>

Accepted formats: PDF, DOC, DOCX, JPG, PNG, GIF
Max file size: 10MB
```

**Response:**
```json
{
  "success": true,
  "data": {
    "publicId": "draft_abc123def456",
    "url": "https://cloudinary.example.com/draft/abc123def456.pdf",
    "fileName": "student_cv.pdf",
    "size": 2048576
  }
}
```

---

### 2. Delete Draft File
**Path:** `DELETE /api/v1/uploads/draft/{publicId}`  
**Auth Required:** ✅ Yes  
**Status:** ✅ Implemented  
**Frontend Status:** ⚠️ Needs to update path

**Request:**
```
Path Param: publicId = draft_abc123def456
```

**Response:**
```json
{
  "success": true,
  "data": "File deleted successfully"
}
```

---

## 🏢 ADMIN ENDPOINTS

### 1. Get Pending Faculties
**Path:** `GET /api/admin/pending-faculties`  
**Auth Required:** ✅ Yes (Admin)  
**Status:** ✅ Implemented

**Request:**
```
Query Params: ?page=0&size=20
```

**Response:**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440003",
        "name": "Faculty of Engineering",
        "abbreviation": "FE",
        "email": "engineering@university.edu.ma",
        "status": "PENDING"
      }
    ],
    "totalElements": 5,
    "totalPages": 1,
    "currentPage": 0
  }
}
```

---

### 2. Approve Faculty
**Path:** `POST /api/admin/approve-faculty/{id}`  
**Auth Required:** ✅ Yes (Admin)  
**Status:** ✅ Implemented

**Request:**
```
Path Param: id = 550e8400-e29b-41d4-a716-446655440003
```

**Response:**
```json
{
  "success": true,
  "data": "Faculty approved successfully"
}
```

---

### 3. Reject Faculty
**Path:** `POST /api/admin/reject-faculty/{id}`  
**Auth Required:** ✅ Yes (Admin)  
**Status:** ✅ Implemented

**Request:**
```
Path Param: id = 550e8400-e29b-41d4-a716-446655440003
```

**Response:**
```json
{
  "success": true,
  "data": "Faculty rejected successfully"
}
```

---

## ⚠️ ENDPOINTS NOT IMPLEMENTED

### Frontend Calls These But Backend Doesn't Have:

#### 1. Student Approval (Admin)
**Frontend expects:** `POST /api/admin/students/{id}/approve`  
**Backend:** ❌ Not implemented  
**Solution:** Implement student approval endpoint

#### 2. Student Rejection (Admin)
**Frontend expects:** `POST /api/admin/students/{id}/reject`  
**Backend:** ❌ Not implemented  
**Solution:** Implement student rejection endpoint

#### 3. Get Pending Students (Admin)
**Frontend expects:** `GET /api/admin/students/pending`  
**Backend:** ❌ Not implemented  
**Solution:** Implement get pending students endpoint

#### 4. Get Pending Users (Auth)
**Backend has:** `GetPendingUsersQueryHandler` but no controller endpoint  
**Solution:** Add controller endpoint: `GET /api/auth/pending-users`

#### 5. Approve User (Auth)
**Backend has:** `ApproveUserCommandHandler` but no controller endpoint  
**Solution:** Add controller endpoint: `POST /api/auth/{id}/approve`

---

## 🔄 FRONTEND API CLIENT FIXES NEEDED

### File: `frontend/src/services/auth.service.ts`

**Issues:**
1. ✅ Auth endpoints are correct (`/api/auth/...`)
2. ⚠️ Profile endpoints call non-existent backend routes
3. ⚠️ Admin endpoints call non-existent routes

**Required Updates:**
```typescript
// Update all v1 paths:
// FROM: /api/profile/...
// FROM: /api/admin/...

// Example fixes needed:
// FROM: '/api/profile/student' 
// TO: '/api/v1/students/me' (GET) or create PUT endpoint

// FROM: '/api/admin/students/pending'
// TO: Need backend implementation

// FROM: '/api/admin/students/{id}/approve'
// TO: Need backend implementation
```

### File: `frontend/src/services/api.ts` 

**Issues:**
1. ✅ Axios instance properly configured
2. ✅ Authorization header handling correct
3. ✅ Token refresh logic implemented
4. ⚠️ No environment-based baseURL configuration mentioned

### All Components Calling API

**Files to update:**
- `frontend/src/features/pfe/**/*.tsx` - Update `/api/projects` → `/api/v1/projects`
- `frontend/src/features/admin/**/*.tsx` - Implement missing student approval endpoints
- `frontend/src/features/student/**/*.tsx` - Update paths to `/api/v1/`
- `frontend/src/features/advisor/**/*.tsx` - Update paths to `/api/v1/`

---

## 📋 ACTION ITEMS

### Backend (Missing Implementations)

- [ ] Implement `PUT /api/v1/students/{id}` - Update student profile
- [ ] Implement `PUT /api/v1/advisors/{id}` - Update advisor profile
- [ ] Implement `POST /api/v1/students/{id}/approve` - Admin approve student
- [ ] Implement `POST /api/v1/students/{id}/reject` - Admin reject student
- [ ] Implement `GET /api/v1/students/pending` - Get pending students
- [ ] Implement `POST /api/v1/students/{id}/change-password` - Change password
- [ ] Add controller for `GetPendingUsersQueryHandler`
- [ ] Add controller for `ApproveUserCommandHandler`

### Frontend (Update API Calls)

- [ ] Update all project endpoints: `/api/projects` → `/api/v1/projects`
- [ ] Update all faculty endpoints: `/api/faculties` → `/api/v1/faculties`
- [ ] Update all student endpoints: `/api/students` → `/api/v1/students`
- [ ] Update all advisor endpoints: `/api/advisors` → `/api/v1/advisors`
- [ ] Update all upload endpoints: `/api/uploads` → `/api/v1/uploads`
- [ ] Implement profile update service methods
- [ ] Implement admin student approval service methods
- [ ] Test all endpoints against backend

---

## 🧪 TESTING CHECKLIST

### Auth Flow
- [ ] Login with valid credentials
- [ ] Login with invalid credentials
- [ ] Register as student
- [ ] Register as advisor
- [ ] Email verification
- [ ] Password reset flow (forgot → verify OTP → reset)
- [ ] Token refresh on expiration

### Project Management
- [ ] Create project
- [ ] Invite friend to project
- [ ] Accept invitation
- [ ] Reject invitation
- [ ] Get my project
- [ ] Get pending invitations

### Advisor Selection
- [ ] Submit selection request
- [ ] Get student selection
- [ ] Get advisor students
- [ ] Approve selection
- [ ] Reject selection

### File Upload
- [ ] Upload valid file
- [ ] Upload invalid file
- [ ] Delete file

### Admin Functions
- [ ] Get pending faculties
- [ ] Approve faculty
- [ ] Reject faculty

---

## 📚 API Response Format

All responses follow this standard format:

### Success Response
```json
{
  "success": true,
  "data": { /* actual data */ },
  "timestamp": "2026-05-07T14:30:00Z"
}
```

### Error Response
```json
{
  "success": false,
  "error": {
    "code": "ErrorCode",
    "message": "Human readable error message",
    "details": { /* optional error details */ }
  },
  "timestamp": "2026-05-07T14:30:00Z"
}
```

---

## 🔐 Authentication

**Header Format:**
```
Authorization: Bearer {access_token}
```

**Token Storage (Frontend):**
- Access Token: localStorage (HttpOnly preferred)
- Refresh Token: Cookie (HttpOnly, Secure, SameSite)

**Token Expiration:**
- Access Token: 15 minutes (configurable)
- Refresh Token: 7 days (configurable)

---

## 📊 API Version & Base URL

**Base URL (Development):**
```
http://localhost:8081
```

**Base URL (Production):**
```
https://api.heysir.example.com
```

**API Versioning:**
- Auth endpoints: `/api/auth/...` (no version)
- Academic endpoints: `/api/v1/...` (versioned)
- Admin endpoints: `/api/admin/...` (no version)

---

**Document Generated:** May 7, 2026  
**Last Updated:** May 7, 2026  
**Status:** Ready for Backend Implementation & Frontend Updates
