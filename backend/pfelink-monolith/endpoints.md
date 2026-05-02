# PFE-Link API Endpoints

This document provides a comprehensive list of all backend endpoints available in the PFE-Link platform.

## 🔐 Authentication & Admin
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/auth/signup/student` | Register a new student |
| `POST` | `/api/auth/signup/advisor` | Register a new advisor |
| `POST` | `/api/auth/login` | User login (JWT) |
| `POST` | `/api/auth/verify-email` | Verify email with token |
| `POST` | `/api/auth/forgot-password` | Request password reset |
| `POST` | `/api/auth/verify-otp` | Verify OTP for password reset |
| `POST` | `/api/auth/reset-password` | Reset password using OTP |
| `GET` | `/api/admin/pending-faculties` | List faculties awaiting approval |
| `POST` | `/api/admin/approve-faculty/{id}` | Approve a faculty membership |
| `POST` | `/api/admin/reject-faculty/{id}` | Reject a faculty membership |
| `POST` | `/api/admin/seasons` | Create a new academic season |
| `GET` | `/api/admin/seasons/active` | Get the currently active season |
| `PUT` | `/api/admin/seasons/{id}/activate` | Activate a specific season |

## 📁 Storage & Notifications
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/v1/uploads/draft` | Upload a file to temporary storage |
| `DELETE` | `/api/v1/uploads/draft/{publicId}` | Delete a file from storage |
| `GET` | `/api/notifications/{userId}` | Get paginated notifications for a user |
| `GET` | `/api/notifications/{userId}/unread-count` | Get unread notification count |
| `PATCH` | `/api/notifications/{userId}/read/{id}` | Mark a notification as read |
| `PATCH` | `/api/notifications/{userId}/read-all` | Mark all notifications as read |

## 🎓 Academic (Faculties & Persons)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/v1/faculties` | List all available faculties |
| `POST` | `/api/v1/faculties` | Create a new faculty |
| `POST` | `/api/faculty-assignments` | Submit assignment request to a faculty |
| `GET` | `/api/v1/advisors/faculty/{id}` | Get approved advisors for a faculty |
| `GET` | `/api/v1/students/me` | Get current student profile |

## 🚀 Project Management & Selection
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/v1/projects` | Create a new PFE project |
| `POST` | `/api/v1/projects/invite` | Invite a student to a project group |
| `POST` | `/api/v1/projects/invitations/{id}/accept` | Accept a project invitation |
| `GET` | `/api/v1/projects/student/{userId}` | Get project details for a student |
| `POST` | `/api/v1/selection-requests` | Submit selection request to an advisor |
| `GET` | `/api/v1/selection-requests/student/{id}` | Get active selection for a student |
| `GET` | `/api/v1/selection-requests/advisor/{advisorUserId}/faculty/{facultyId}` | Get students for an advisor |
| `PUT` | `/api/v1/selection-requests/{id}/approve` | Advisor approves student selection |
| `PUT` | `/api/v1/selection-requests/{id}/reject` | Advisor rejects student selection |

## ✉️ Email Actions (HTML Responses)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/faculty-action/approve/{id}` | Approve faculty via email link |
| `GET` | `/api/faculty-action/reject/{id}` | Reject faculty via email link |
| `GET` | `/api/faculty-action/approve-advisor` | Approve advisor assignment via email |
| `GET` | `/api/faculty-action/reject-advisor` | Reject advisor assignment via email |
| `GET` | `/api/faculty-action/approve-student` | Approve student profile via email |
| `GET` | `/api/faculty-action/reject-student` | Reject student profile via email |
| `GET` | `/api/faculty-action/approve-selection` | Approve selection request via email |
| `GET` | `/api/faculty-action/reject-selection` | Reject selection request via email |

---

## 📝 Request Payload Examples

### 🔐 Authentication
**POST `/api/auth/signup/student`**
```json
{
  "email": "student@heysir.com",
  "password": "securePassword123",
  "fullName": "Adam Smith",
  "telephone": "21612345678",
  "cinNumber": "01234567",
  "studentCardUrl": "https://res.cloudinary.com/.../card.jpg",
  "draftId": "draft_789",
  "facultyId": "fac_123"
}
```

**POST `/api/auth/signup/advisor`**
```json
{
  "email": "advisor@heysir.com",
  "password": "securePassword123",
  "fullName": "Dr. Sarah Connor",
  "telephone": "21698765432",
  "cinNumber": "76543210",
  "cinCardUrl": "https://res.cloudinary.com/.../cin.jpg",
  "draftId": "draft_456"
}
```

**POST `/api/auth/login`**
```json
{
  "email": "user@heysir.com",
  "password": "mySecretPassword"
}
```

**POST `/api/auth/verify-otp`**
```json
{
  "email": "user@heysir.com",
  "otp": "123456"
}
```

**POST `/api/auth/reset-password`**
```json
{
  "email": "user@heysir.com",
  "otp": "123456",
  "newPassword": "newSecurePassword789"
}
```

### 🎓 Academic & Admin
**POST `/api/admin/seasons`**
```json
{
  "year": "2024/2025",
  "startDate": "2024-09-01",
  "endDate": "2025-06-30"
}
```

**POST `/api/v1/faculties`**
```json
{
  "name": "Faculty of Sciences of Tunis",
  "abbreviation": "FST",
  "email": "contact@fst.utm.tn",
  "websiteUrl": "http://www.fst.rnu.tn",
  "imageUrl": "https://res.cloudinary.com/demo/image/upload/v1234567890/fst_logo.png",
  "adminPassword": "securePassword123",
  "path": "Tunisia.Tunis.El_Manar.Campus_Universitaire"
}
```

**POST `/api/admin/approve-faculty/{id}`**
```json
// Empty Body
{}
```

**POST `/api/faculty-assignments`**
```json
{
  "facultyId": "550e8400-e29b-41d4-a716-446655440000",
  "advisorProfileId": "660f9511-f30c-52e5-b827-557766551111",
  "message": "I would like to join the Faculty of Sciences as an advisor."
}
```

### 🚀 Project Management
**POST `/api/v1/projects`**
```json
{
  "title": "Smart City IoT Dashboard",
  "description": "A real-time monitoring system for city-wide IoT sensors using Spring Boot and React."
}
```

**POST `/api/v1/projects/invite`**
```json
{
  "projectId": "550e8400-e29b-41d4-a716-446655440000",
  "email": "friend@student.com"
}
```

**POST `/api/v1/selection-requests`**
```json
{
  "projectId": "550e8400-e29b-41d4-a716-446655440000",
  "advisorProfileId": "660f9511-f30c-52e5-b827-557766551111",
  "message": "I am very interested in your IoT project and have experience with MQTT."
}
```

**PUT `/api/v1/selection-requests/{id}/approve`**
```json
// Empty Body
{}
```

### 📁 Storage & Notifications
**POST `/api/v1/uploads/draft`**
```http
Content-Type: multipart/form-data; boundary=boundary

--boundary
Content-Disposition: form-data; name="file"; filename="document.pdf"
Content-Type: application/pdf

[Binary Content]
--boundary--
```

**PATCH `/api/notifications/{userId}/read-all`**
```json
// Empty Body
{}
```

