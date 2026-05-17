export { UserRole, AccountStatus, StudentStatus, VerificationStatus, AdvisorStatus } from './enums';
import type { UserRole, AccountStatus } from './enums';

// ============================================
// REQUEST TYPES
// ============================================

export type LoginRequest = {
  email: string;
  password: string;
};

export type StudentRegistrationRequest = {
  email: string;
  password: string;
  fullName: string;
  telephone: string;
  cinNumber: string;
  facultyId: string;
};

export type AdvisorRegistrationRequest = {
  email: string;
  password: string;
  fullName: string;
  telephone: string;
};

export type ForgotPasswordRequest = {
  email: string;
};

export type VerifyOtpRequest = {
  email: string;
  otp: string;
};

export type ResetPasswordRequest = {
  token: string;
  newPassword: string;
};

export type ChangePasswordRequest = {
  currentPassword: string;
  newPassword: string;
};

export type RefreshTokenRequest = {
  refreshToken?: string;
};

export type UpdateStudentProfileRequest = {
  fullName?: string;
  telephone?: string;
  facultyId?: string;
};

export type UpdateAdvisorProfileRequest = {
  fullName?: string;
  telephone?: string;
};

// ============================================
// RESPONSE TYPES
// ============================================

export type AuthResponse = {
  id?: string;
  userId?: string;
  email?: string;
  fullName?: string;
  role?: UserRole;
  status?: AccountStatus;
  token?: string;
  accessToken?: string;
  refreshToken?: string;
};

// Refresh response - tokens only (no user info)
export type RefreshTokenResponse = {
  accessToken: string;
  refreshToken: string;
};

// ============================================
// USER TYPE
// ============================================

export type User = {
  id: string;
  email: string;
  fullName: string;
  role: UserRole;
  status: AccountStatus;
  emailVerified?: boolean;
  imageUrl?: string;
  telephone?: string;
  studentProfile?: StudentProfile;
  advisorProfile?: AdvisorProfile;
};

export type StudentProfile = {
  id: string;
  fullName: string;
  studentCardUrl?: string;
  facultyName?: string;
  facultyLocation?: string;
  verificationStatus: string;
};

export type AdvisorProfile = {
  id: string;
  fullName: string;
  telephone?: string;
  specialization?: string;
  department?: string;
};
