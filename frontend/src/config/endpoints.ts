/**
 * Centralized API endpoint paths.
 * Base URL lives in env (VITE_API_URL); paths live here.
 * Never hardcode an API path elsewhere - import from this file.
 */

export const AUTH_ENDPOINTS = {
  login:          '/api/auth/login',
  refresh:        '/api/auth/refresh',
  logout:         '/api/auth/logout',
  me:             '/api/auth/me',
  signupStudent:  '/api/auth/signup/student',
  signupAdvisor:  '/api/auth/signup/advisor',
  verifyEmail:    '/api/auth/verify-email',
  verifyOtp:      '/api/auth/verify-otp',
  resendVerify:   '/api/auth/resend-verification',
  forgotPassword: '/api/auth/forgot-password',
  resetPassword:  '/api/auth/reset-password',
} as const;

export const ACADEMIC_ENDPOINTS = {
  faculties:          '/api/v1/faculties',
  facultyAssignments: '/api/faculty-assignments',
  studentsMe:         '/api/v1/students/me',
  studentsSearch:     '/api/v1/students/search',
  pendingFaculties:   '/api/admin/pending-faculties',
  selectionRequests:  '/api/v1/selection-requests',
  advisorsByFaculty:  (facultyId: string) => `/api/v1/advisors/faculty/${facultyId}`,
  approveFaculty:     (id: string) => `/api/admin/approve-faculty/${id}`,
  rejectFaculty:      (id: string) => `/api/admin/reject-faculty/${id}`,
} as const;

export const PROJECT_ENDPOINTS = {
  create:                '/api/v1/projects',
  myProject:             '/api/v1/projects/me',
  invite:                '/api/v1/projects/invite',
  invitations:           '/api/v1/projects/invitations/me',
  acceptInvitation:      (id: string) => `/api/v1/projects/invitations/${id}/accept`,
  rejectInvitation:      (id: string) => `/api/v1/projects/invitations/${id}/reject`,
  selectionRequests:     '/api/v1/selection-requests',
  mySelection:           (userId: string) => `/api/v1/selection-requests/student/${userId}`,
  uploadDraft:           '/api/v1/uploads/draft',
  deleteDraft:           (publicId: string) => `/api/v1/uploads/draft/${publicId}`,
} as const;
