import api from './api';
import type { 
  AuthResponse, 
  StudentRegistrationRequest, 
  AdvisorRegistrationRequest,
  LoginRequest,
  ForgotPasswordRequest,
  VerifyOtpRequest,
  ResetPasswordRequest,
  ChangePasswordRequest,
  UpdateStudentProfileRequest,
  UpdateAdvisorProfileRequest,
  StudentProfile,
  User 
} from '../types/auth.types';

/**
 * Authentication Service
 * Handles all auth-related API calls to the backend
 */
export const authService = {
  // ============================================
  // AUTHENTICATION ENDPOINTS
  // ============================================

  /**
   * Login user
   * POST /api/auth/login
   */
  login: async (credentials: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>('/api/auth/login', credentials);
    if (response.data.accessToken) {
      localStorage.setItem('accessToken', response.data.accessToken);
      localStorage.setItem('user', JSON.stringify({
        id: response.data.userId,
        email: response.data.email,
        role: response.data.role,
        status: response.data.status
      }));
    }
    return response.data;
  },

  /**
   * Register student
   * POST /api/auth/signup/student
   */
  registerStudent: async (data: StudentRegistrationRequest): Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>('/api/auth/signup/student', data);
    return response.data;
  },

  /**
   * Register advisor
   * POST /api/auth/signup/advisor
   */
  registerAdvisor: async (data: AdvisorRegistrationRequest): Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>('/api/auth/signup/advisor', data);
    if (response.data.accessToken) {
      localStorage.setItem('accessToken', response.data.accessToken);
      localStorage.setItem('user', JSON.stringify({
        id: response.data.userId,
        email: response.data.email,
        role: response.data.role,
        status: response.data.status
      }));
    }
    return response.data;
  },

  /**
   * Verify email
   * GET /api/auth/verify-email
   */
  verifyEmail: async (token: string): Promise<string> => {
    const response = await api.get<string>('/api/auth/verify-email', {
      params: { token }
    });
    return response.data;
  },

  /**
   * Resend verification email
   * POST /api/auth/resend-verification
   */
  resendVerification: async (email: string): Promise<string> => {
    const response = await api.post<string>('/api/auth/resend-verification', null, {
      params: { email }
    });
    return response.data;
  },

  // ============================================
  // PASSWORD RESET ENDPOINTS
  // ============================================

  /**
   * Request password reset OTP
   * POST /api/auth/forgot-password
   */
  forgotPassword: async (email: string): Promise<string> => {
    const response = await api.post<string>('/api/auth/forgot-password', { email } as ForgotPasswordRequest);
    return response.data;
  },

  /**
   * Verify OTP code
   * POST /api/auth/verify-otp
   */
  verifyOtp: async (email: string, otpCode: string): Promise<string> => {
    const response = await api.post<string>('/api/auth/verify-otp', { email, otpCode } as VerifyOtpRequest);
    return response.data;
  },

  /**
   * Reset password with OTP
   * POST /api/auth/reset-password
   */
  resetPassword: async (data: ResetPasswordRequest): Promise<string> => {
    const response = await api.post<string>('/api/auth/reset-password', data);
    return response.data;
  },

  // ============================================
  // SESSION MANAGEMENT
  // ============================================

  /**
   * Logout user
   */
  logout: () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('user');
  },

  /**
   * Get current user from localStorage
   */
  getCurrentUser: (): User | null => {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      try {
        return JSON.parse(userStr);
      } catch {
        return null;
      }
    }
    return null;
  },

  /**
   * Check if user is authenticated
   */
  isAuthenticated: (): boolean => {
    return !!localStorage.getItem('accessToken');
  },

  /**
   * Get access token
   */
  getAccessToken: (): string | null => {
    return localStorage.getItem('accessToken');
  }
};

/**
 * Profile Service
 * Handles profile management API calls
 */
export const profileService = {
  /**
   * Update student profile
   * PUT /api/profile/student
   */
  updateStudentProfile: async (data: UpdateStudentProfileRequest): Promise<string> => {
    const response = await api.put<string>('/api/profile/student', data);
    return response.data;
  },

  /**
   * Update advisor profile
   * PUT /api/profile/advisor
   */
  updateAdvisorProfile: async (data: UpdateAdvisorProfileRequest): Promise<string> => {
    const response = await api.put<string>('/api/profile/advisor', data);
    return response.data;
  },

  /**
   * Change password
   * POST /api/profile/change-password
   */
  changePassword: async (data: ChangePasswordRequest): Promise<string> => {
    const response = await api.post<string>('/api/profile/change-password', data);
    return response.data;
  }
};

/**
 * Admin Service
 * Handles admin operations for student management
 */
export const adminService = {
  /**
   * Get pending students
   * GET /api/admin/students/pending
   */
  getPendingStudents: async (): Promise<StudentProfile[]> => {
    const response = await api.get<StudentProfile[]>('/api/admin/students/pending');
    return response.data;
  },

  /**
   * Approve student
   * POST /api/admin/students/{id}/approve
   */
  approveStudent: async (id: string): Promise<Record<string, string>> => {
    const response = await api.post<Record<string, string>>(`/api/admin/students/${id}/approve`);
    return response.data;
  },

  /**
   * Reject student
   * POST /api/admin/students/{id}/reject
   */
  rejectStudent: async (id: string, reason: string): Promise<Record<string, string>> => {
    const response = await api.post<Record<string, string>>(`/api/admin/students/${id}/reject`, { reason });
    return response.data;
  }
};
