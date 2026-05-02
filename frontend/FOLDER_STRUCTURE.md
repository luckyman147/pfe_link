# Frontend Folder Structure

## Feature-Based Architecture

This project follows a **feature-based architecture** where each feature is self-contained with its own components, pages, hooks, services, and routes.

## Folder Structure

```
src/
├── app/                        # App-level setup
│   ├── App.tsx                 # Main App component
│   ├── providers.tsx           # All providers wrapper
│   └── router.tsx              # Main router configuration
│
├── features/                   # Feature modules (main folder)
│   ├── auth/                   # Authentication feature
│   │   ├── components/         # Auth-specific components
│   │   ├── hooks/              # Auth hooks (useAuth, useLogin)
│   │   ├── pages/              # Auth pages (Login, Signup, etc.)
│   │   ├── services/           # Auth API services
│   │   ├── types/              # Auth-specific types
│   │   └── index.ts            # Public exports
│   │
│   ├── landing/                # Landing/Marketing feature
│   │   ├── components/         # Landing-specific components
│   │   ├── sections/           # Landing page sections
│   │   ├── pages/              # Landing pages
│   │   └── index.ts
│   │
│   ├── student/                # Student dashboard feature
│   │   ├── components/
│   │   ├── hooks/
│   │   ├── pages/
│   │   ├── services/
│   │   └── index.ts
│   │
│   ├── advisor/                # Advisor dashboard feature
│   │   ├── components/
│   │   ├── hooks/
│   │   ├── pages/
│   │   ├── services/
│   │   └── index.ts
│   │
│   ├── admin/                  # Admin dashboard feature
│   │   ├── components/
│   │   ├── hooks/
│   │   ├── pages/
│   │   ├── services/
│   │   └── index.ts
│   │
│   └── pfe/                    # PFE management feature
│       ├── components/
│       ├── hooks/
│       ├── pages/
│       ├── services/
│       └── index.ts
│
├── shared/                     # Shared/Common code
│   ├── components/             # Reusable components
│   │   ├── ui/                 # UI primitives (Button, Input, etc.)
│   │   └── layout/             # Layout components (Navbar, Footer)
│   ├── hooks/                  # Shared hooks
│   ├── services/               # Shared services (API client)
│   ├── types/                  # Shared types
│   ├── utils/                  # Utility functions
│   └── lib/                    # Third-party integrations
│
├── config/                     # App configuration
│   ├── env.ts                  # Environment variables
│   └── constants.ts            # App constants
│
├── i18n/                       # Internationalization
│   ├── locales/
│   │   ├── en.json
│   │   └── fr.json
│   └── index.ts
│
├── styles/                     # Global styles
│   └── index.css               # Main CSS file
│
├── assets/                     # Static assets
│   ├── images/
│   └── icons/
│
└── main.tsx                    # Entry point
```

## Feature Module Structure

Each feature follows this internal structure:

```
features/[feature-name]/
├── components/                 # Feature-specific components
│   ├── ComponentA.tsx
│   └── ComponentB.tsx
│
├── hooks/                      # Feature-specific hooks
│   ├── useFeatureData.ts
│   └── useFeatureActions.ts
│
├── pages/                      # Feature pages
│   ├── MainPage.tsx
│   └── DetailPage.tsx
│
├── services/                   # Feature API services
│   └── feature.service.ts
│
├── types/                      # Feature-specific types
│   └── feature.types.ts
│
├── utils/                      # Feature-specific utilities (optional)
│   └── helpers.ts
│
└── index.ts                    # Public exports (barrel file)
```

## Naming Conventions

- **Components**: PascalCase (`LoginForm.tsx`, `StudentDashboard.tsx`)
- **Hooks**: camelCase with `use` prefix (`useAuth.ts`, `usePFEs.ts`)
- **Services**: camelCase with `.service` suffix (`auth.service.ts`)
- **Types**: camelCase with `.types` suffix (`auth.types.ts`)
- **Pages**: PascalCase (`Login.tsx`, `Dashboard.tsx`)

## Import Rules

1. **Feature imports**: Use barrel exports (`import { useAuth } from '@/features/auth'`)
2. **Shared imports**: Import from shared folder (`import { Button } from '@/shared/components/ui'`)
3. **Relative imports**: Only within the same feature

## Example Feature (Auth)

```
features/auth/
├── components/
│   ├── LoginForm.tsx
│   ├── SignupForm.tsx
│   └── PasswordInput.tsx
│
├── hooks/
│   ├── useAuth.ts
│   └── useLogin.ts
│
├── pages/
│   ├── Login.tsx
│   ├── StudentSignup.tsx
│   ├── AdvisorSignup.tsx
│   ├── ForgotPassword.tsx
│   └── UserTypeSelection.tsx
│
├── services/
│   └── auth.service.ts
│
├── types/
│   └── auth.types.ts
│
└── index.ts
```
