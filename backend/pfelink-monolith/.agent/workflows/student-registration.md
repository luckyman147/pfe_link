---
description: How to register a new student and follow through to faculty approval
---

# Student Registration Workflow

This workflow describes the process for a student to sign up, verify their email, and obtain faculty approval to access the platform.

## 1. Initial Signup
The student provides their details via the registration form.

**Endpoint**: `POST /api/auth/signup/student`

**Payload**:
```json
{
  "email": "student@example.com",
  "password": "SecurePassword123!",
  "fullName": "Fatima Alami",
  "telephone": "+21612345678",
  "cinNumber": "12345678",
  "studentCardUrl": "https://storage.example.com/cards/fatima.jpg",
  "facultyId": "UUID-OF-FACULTY"
}
```

**Outcome**:
- A User account is created with status `PENDING`.
- An initial Student Profile is created.
- A verification email is sent to the student.

## 2. Email Verification
The student clicks the link in their email.

**Endpoint**: `GET /api/auth/verify-email?token={token}`

**Outcome**:
- The User's `emailVerified` flag is set to `true`.
- The faculty is automatically notified via email that a new student profile requires approval.

## 3. Faculty Approval
The faculty administrator receives an interactive email.

**Actions**:
- **Approve**: Faculty clicks "Approve Student" in the email.
- **Reject**: Faculty clicks "Reject Student" in the email.

**Outcome (on Approval)**:
- The Student Profile status is updated to `APPROVED`.
- The User account status is updated to `ACTIVE`.
- The student receives an email confirming their approval.

## 4. Platform Access
Once approved and active, the student can log in via `/api/auth/login` and access the dashboard.
