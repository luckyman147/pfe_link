import api from '@/shared/services/api';
import type { ForgotPasswordRequest, VerifyOtpRequest, ResetPasswordRequest } from '../types/auth.types';

export const recoveryService = {
  forgotPassword: async (email: string): Promise<string> => {
    const response = await api.post<string>('/api/auth/forgot-password', { email } as ForgotPasswordRequest);
    return response.data;
  },

  verifyOtp: async (email: string, otpCode: string): Promise<string> => {
    const response = await api.post<string>('/api/auth/verify-otp', { email, otpCode } as VerifyOtpRequest);
    return response.data;
  },

  resetPassword: async (data: ResetPasswordRequest): Promise<string> => {
    const response = await api.post<string>('/api/auth/reset-password', data);
    return response.data;
  }
};
