---
description: How to register a new advisor and join a faculty
---

# Advisor Registration Workflow

This workflow describes the process for an advisor to sign up, verify their email, and request assignment to one or more faculties.

## 1. Initial Account Signup
The advisor creates their base account.

**Endpoint**: `POST /api/auth/signup/advisor`

**Payload**:
```json
{
  "email": "advisor@example.com",
  "password": "SecurePassword123!",
  "fullName": "Youssef Alami",
  "telephone": "+21699887766",
  "cinNumber": "87654321",
  "cinCardUrl": "https://storage.example.com/cards/youssef_cin.jpg"
}
```

**Outcome**:
- A User account is created with status `PENDING`.
- An initial Advisor Profile is created.
- A verification email is sent to the advisor.

## 2. Email Verification
The advisor clicks the link in their email.

**Endpoint**: `GET /api/auth/verify-email?token={token}`

**Outcome**:
- The User's `emailVerified` flag is set to `true`.
- The advisor can now log in but their account remains `PENDING` until a faculty assignment is approved.

## 3. Submit Faculty Assignment Request
Once logged in, the advisor must request to join a faculty for the active academic season.

**Endpoint**: `POST /api/faculty-assignments`

**Payload**:
```json
{
  "advisorId": "UUID-OF-ADVISOR-PROFILE",
  "facultyId": "UUID-OF-FACULTY",
  "advisorRole": "PROFESSOR",
  "maxCapacity": 5,
  "facultyDomainEmail": "youssef.alami@faculty.tn",
  "professionalProofUrl": "https://storage.example.com/proofs/youssef_proof.pdf"
}
```

**Outcome**:
- A `FacultyAssignment` is created with status `PENDING`.
- The faculty administrator receives an interactive email with the advisor's full details (CIN, telephone, proof etc.).

## 4. Administrative Approval
The faculty administrator clicks "Approve Advisor" in their email.

**Outcome**:
- The `FacultyAssignment` status is updated to `APPROVED`.
- The User's account status is updated to `ACTIVE`.
- The advisor receives an email confirming they are now part of the faculty.

## 5. Ready for Selection
The advisor now appears in the search results for students within that faculty and can start receiving selection requests.
