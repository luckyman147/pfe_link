# pfe_link

> A full-stack platform for managing **Final Year Projects (PFE — Projet de Fin d'Études)** in academic institutions. It connects students, advisors, and administrators through a structured, workflow-driven experience.

---

## 📋 Table of Contents

- [Project Overview](#-project-overview)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Features](#-features)
- [Progress Tracker](#-progress-tracker)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [Environment Variables](#-environment-variables)

---

## 🧭 Project Overview

**HeySir**  is a web platform designed to digitize and streamline the PFE process in higher education. It manages the full lifecycle of a final-year project — from student registration and advisor assignment to project proposal, selection, and administrative approval.

### Key Actors

| Role | Description |
|------|-------------|
| **Student** | Registers, forms groups, submits project proposals, selects advisors |
| **Advisor** | Registers, is assigned to faculties/seasons, reviews and supervises projects |
| **Admin** | Approves users, manages faculties, seasons, and assignments |

---

## 🛠 Tech Stack

### Backend
| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Core language |
| Spring Boot | 3.4.2 | Application framework |
| Spring Security | — | Authentication & authorization |
| Spring Data JPA | — | ORM / data access layer |
| PostgreSQL | 16 | Relational database |
| JWT (jjwt) | 0.12.3 | Stateless authentication tokens |
| Spring Mail | — | Email notifications & OTP delivery |
| SpringDoc OpenAPI | 2.8.4 | Swagger UI / API documentation |
| Lombok | — | Boilerplate reduction |

### Frontend
| Technology | Version | Purpose |
|---|---|---|
| React | 19 | UI framework |
| TypeScript | 5.9 | Type safety |
| Vite | 7 | Build tool & dev server |
| TailwindCSS | 4 | Styling |
| React Router DOM | 7 | Client-side routing |
| TanStack Query | 5 | Server state management |
| React Hook Form | 7 + Zod | Form management & validation |
| Axios | — | HTTP client |
| i18next | — | Internationalization (EN/FR) |
| Socket.io Client | 4 | Real-time communication |
| Lucide React | — | Icon library |

### Infrastructure
| Tool | Purpose |
|---|---|
| Docker + Docker Compose | Containerized local development |
| PostgreSQL (Docker) | Database container |

---

## 🏗 Architecture

### Backend — Clean Architecture (4-Layer Monolith)

```
pfelink-monolith/
├── domain/          # Entities, Value Objects, Repository Interfaces (no frameworks)
├── application/     # Use Cases (Commands & Queries), DTOs, Business Services
├── infrastructure/  # JPA Repositories, Security, Email, Events, Storage
└── api/             # REST Controllers, Request/Response DTOs, Validation
```

The backend strictly follows **Clean Architecture** and **CQRS** patterns:
- All business rules live in `domain` and `application`
- Infrastructure adapters implement domain interfaces
- Controllers are thin — they delegate to command/query handlers

### Frontend — Feature-Based Architecture

```
src/
├── features/       # Self-contained feature modules (auth, student, advisor, admin, pfe, landing)
├── shared/         # Reusable UI components, hooks, services, utilities
├── app/            # App-level setup (router, providers, App.tsx)
├── config/         # Environment variables, constants
├── store/          # Global state management
├── i18n/           # Translations (EN / FR)
└── routes/         # Route definitions
```

---

## ✅ Features

### 🔐 Authentication & User Management
- [x] Student registration with email verification (OTP)
- [x] Advisor registration with email verification (OTP)
- [x] Admin approval workflow for new users
- [x] JWT-based stateless authentication
- [x] Forgot password / reset password flow
- [x] Role-based access control (Student / Advisor / Admin)

### 🏫 Faculty & Season Management
- [x] Admin can create and manage faculties
- [x] Admin can view pending faculty requests
- [x] Admin can approve or reject faculty creation
- [x] Season management (academic year cycles)
- [x] Assign advisors to faculties per season (`FacultyAssignment`)
- [x] Submit / approve / reject faculty assignment requests

### 👥 Academic Profiles
- [x] Student profile management
- [x] Advisor profile management
- [x] List all students and advisors

### 📁 Project Management
- [x] Students can create PFE projects
- [x] Project invitation system (invite other students to a group)
- [x] Project invitation acceptance/rejection
- [x] Advisor assignment to projects (`AdvisorAssignment`)

### 🔎 Selection Requests
- [x] Students can submit selection requests for advisor supervision
- [x] Advisors can view, approve, or reject selection requests
- [x] Selection request listing and filtering

### 🔔 Notifications
- [ ] Real-time notifications via Socket.io (infrastructure scaffold in place)
- [ ] In-app notification center

### 🌍 Internationalization
- [x] English and French language support (i18next)

---

## 📊 Progress Tracker

### Backend Progress

| Module | Domain | Application | Infrastructure | API | Status |
|---|---|---|---|---|---|
| Auth (Register/Login) | ✅ | ✅ | ✅ | ✅ | **Done** |
| Email OTP Verification | ✅ | ✅ | ✅ | ✅ | **Done** |
| Password Reset | ✅ | ✅ | ✅ | ✅ | **Done** |
| Admin User Approval | ✅ | ✅ | ✅ | ✅ | **Done** |
| Faculty Management | ✅ | ✅ | ✅ | ✅ | **Done** |
| Season Management | ✅ | ✅ | ✅ | ✅ | **Done** |
| Faculty Assignment | ✅ | ✅ | ✅ | ✅ | **Done** |
| Student Profile | ✅ | ✅ | ✅ | ✅ | **Done** |
| Advisor Profile | ✅ | ✅ | ✅ | ✅ | **Done** |
| Project Creation | ✅ | ✅ | ✅ | ✅ | **Done** |
| Project Invitations | ✅ | ✅ | ✅ | ✅ | **Done** |
| Advisor Assignment | ✅ | ✅ | ✅ | ✅ | **Done** |
| Selection Requests | ✅ | ✅ | ✅ | ✅ | **Done** |
| Real-time Notifications | ⬜ | ⬜ | 🔧 | ⬜ | **In Progress** |
| File Upload / Storage | ⬜ | ⬜ | 🔧 | ⬜ | **Scaffolded** |

### Frontend Progress

| Feature Module | Pages | Hooks | Services | Status |
|---|---|---|---|---|
| Landing Page | ✅ | — | — | **Done** |
| Auth (Login, Signup, OTP, Reset) | ✅ | ✅ | ✅ | **Done** |
| Student Dashboard | 🔧 | 🔧 | 🔧 | **In Progress** |
| Advisor Dashboard | 🔧 | 🔧 | 🔧 | **In Progress** |
| Admin Dashboard | 🔧 | 🔧 | 🔧 | **In Progress** |
| PFE / Project Management | 🔧 | 🔧 | 🔧 | **In Progress** |

> Legend: ✅ Done · 🔧 In Progress / Scaffolded · ⬜ Not Started

---

## 📂 Project Structure

```
heysir/
├── backend/
│   └── pfelink-monolith/
│       ├── src/main/java/com/pfelink/monolith/
│       │   ├── PfeLinkApplication.java
│       │   ├── domain/
│       │   │   ├── academic/         # Faculty, Season, Project, Selection, Assignment entities & interfaces
│       │   │   └── auth/             # User, OTP entities & interfaces
│       │   ├── application/
│       │   │   ├── academic/         # Commands: create_faculty, season, project, selection, assignment...
│       │   │   └── auth/             # Commands: register, login, verify_otp, reset_password...
│       │   ├── infrastructure/
│       │   │   ├── persistence/      # JPA repository implementations
│       │   │   ├── security/         # JWT filter, SecurityConfig
│       │   │   ├── email/            # Email service implementation
│       │   │   ├── event/            # Domain event handling
│       │   │   └── storage/          # File storage scaffold
│       │   └── api/
│       │       ├── academic/         # Faculty, Project, Selection, Season, Person controllers
│       │       ├── auth/             # Auth controller
│       │       └── notification/     # Notification controller
│       └── Dockerfile
│
├── frontend/
│   ├── src/
│   │   ├── features/
│   │   │   ├── auth/                 # Login, Signup, OTP, Password Reset pages
│   │   │   ├── student/              # Student dashboard & features
│   │   │   ├── advisor/              # Advisor dashboard & features
│   │   │   ├── admin/                # Admin dashboard & features
│   │   │   ├── pfe/                  # PFE project management
│   │   │   └── landing/              # Landing/marketing page
│   │   ├── shared/                   # Reusable UI, hooks, services, utils
│   │   ├── app/                      # Router, Providers, App root
│   │   ├── i18n/                     # en.json / fr.json translations
│   │   └── config/                   # env.ts, constants.ts
│   └── Dockerfile
│
└── docker-compose.yml
```

---

## 🚀 Getting Started

### Prerequisites
- Docker & Docker Compose installed
- Java 17+ (for local backend dev)
- Node.js 20+ (for local frontend dev)

### Run with Docker Compose

```bash
# Clone the repo
git clone <repo-url>
cd heysir

# Start all services (PostgreSQL + Backend + Frontend)
docker-compose up --build
```

| Service | URL |
|---|---|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| PostgreSQL | localhost:5433 |

### Local Development

**Backend:**
```bash
cd backend/pfelink-monolith
# Start PostgreSQL via Docker first
docker-compose up postgres -d

./mvnw spring-boot:run
```

**Frontend:**
```bash
cd frontend
npm install
npm run dev
```

---

## 📖 API Documentation

Swagger UI is available at:
```
http://localhost:8080/swagger-ui.html
```

### Key API Groups

| Group | Base Path | Description |
|---|---|---|
| Auth | `/api/auth` | Register, login, OTP, password reset |
| Faculty | `/api/v1/faculties` | Faculty CRUD and approval workflow |
| Season | `/api/v1/seasons` | Academic season management |
| Projects | `/api/v1/projects` | Project creation and invitations |
| Selection | `/api/v1/selection-requests` | Advisor selection requests |
| Students | `/api/v1/students` | Student profile management |
| Advisors | `/api/v1/advisors` | Advisor profile management |

---

## 🔐 Environment Variables

### Backend (via Docker Compose or `.env`)

| Variable | Default | Description |
|---|---|---|
| `DB_HOST` | `postgres` | PostgreSQL host |
| `DB_PORT` | `5432` | PostgreSQL port |
| `DB_USERNAME` | `postgres` | Database username |
| `DB_PASSWORD` | `postgres` | Database password |
| `JWT_SECRET` | `my-super-secret-jwt-key-...` | JWT signing key |
| `EMAIL_USERNAME` | — | SMTP email address |
| `EMAIL_PASSWORD` | — | SMTP email password |
| `ADMIN_PASSWORD` | `AdminPass123` | Default admin password |

### Frontend (`.env` file)

| Variable | Description |
|---|---|
| `VITE_AUTH_API` | Auth API base URL |
| `VITE_PFE_API` | PFE API base URL |

---

## 📌 Notes

- The backend uses **CQRS-style** command and query handlers inside the `application` layer.
- All domain repository interfaces are defined in the `domain` layer; JPA implementations live in `infrastructure`.
- The frontend enforces a **feature-based architecture**: each feature owns its pages, hooks, services, and types.
- The project supports **bilingual UI** (English / French) via `i18next`.

## Why This Project Stands Out

- **Complete Workflow** - Manages the entire PFE process from start to finish
- **Three User Roles** - Students, advisors, and administrators all in one platform
- **Real-Time Collaboration** - Teams work together seamlessly
- **Academic Focus** - Built specifically for educational institutions