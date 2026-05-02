/**
 * Auth types matching the PFE-Link Authentication Service API
 * Based on OpenAPI spec v1.0.0
 */

// ============================================
// ENUMS (using const objects for compatibility)
// ============================================

export const UserRole = {
  ADMIN: 'ADMIN',
  ADVISOR: 'ADVISOR',
  STUDENT: 'STUDENT'
} as const;

export type UserRole = typeof UserRole[keyof typeof UserRole];

export const AccountStatus = {
  ACTIVE: 'ACTIVE',
  PENDING: 'PENDING',
  SUSPENDED: 'SUSPENDED'
} as const;

export type AccountStatus = typeof AccountStatus[keyof typeof AccountStatus];

export const VerificationStatus = {
  PENDING: 'PENDING',
  APPROVED: 'APPROVED',
  REJECTED: 'REJECTED'
} as const;

export type VerificationStatus = typeof VerificationStatus[keyof typeof VerificationStatus];

// ============================================
// REQUEST TYPES
// ============================================

export interface LoginRequest {
  email: string;
  password: string;
}

export interface StudentRegistrationRequest {
  email: string;
  password: string;
  fullName: string;
  telephone?: string;
  studentId?: string;
  facultyName?: string;
  facultyLocation?: string;
}

export interface AdvisorRegistrationRequest {
  email: string;
  password: string;
  fullName: string;
  telephone?: string;
  capacity: number;
  department?: string;
  specialization?: string;
}

export interface ForgotPasswordRequest {
  email: string;
}

export interface VerifyOtpRequest {
  email: string;
  otpCode: string;
}

export interface ResetPasswordRequest {
  email: string;
  otpCode: string;
  newPassword: string;
}

export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
}

export interface UpdateStudentProfileRequest {
  fullName: string;
  facultyName?: string;
  facultyLocation?: string;
  telephone?: string;
}

export interface UpdateAdvisorProfileRequest {
  department?: string;
  specialization?: string;
  telephone?: string;
}

// ============================================
// RESPONSE TYPES
// ============================================

export interface AuthResponse {
  userId?: string;
  email?: string;
  role?: string;
  status?: AccountStatus;
  accessToken?: string;
  message?: string;
}

export interface User {
  id: string;
  email: string;
  password?: string;
  imageUrl?: string;
  telephone?: string;
  role: UserRole;
  status: AccountStatus;
  emailVerified?: boolean;
  emailVerificationToken?: string;
  studentProfile?: StudentProfile;
  advisorProfile?: AdvisorProfile;
  createdAt?: string;
  updatedAt?: string;
}

export interface StudentProfile {
  id: string;
  user?: User;
  fullName: string;
  studentCardUrl?: string;
  facultyName?: string;
  facultyLocation?: string;
  verificationStatus: VerificationStatus;
  rejectionReason?: string;
  verifiedBy?: User;
  verifiedAt?: string;
}

export interface AdvisorProfile {
  id: string;
  user?: User;
  fullName: string;
  capacity: number;
  currentStudents?: number;
  department?: string;
  specialization?: string;
}
