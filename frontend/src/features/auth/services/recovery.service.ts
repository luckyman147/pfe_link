import api from '@/shared/services/api';
import { AUTH_ENDPOINTS } from '@/config/endpoints';
import type { ApiResponse } from '@/shared/types/api';
import type { ForgotPasswordRequest, VerifyOtpRequest, ResetPasswordRequest } from '../types/auth.types';

export const recoveryService = {
  forgotPassword: async (email: string): Promise<string> => {
    const response = await api.post<ApiResponse<string>>(AUTH_ENDPOINTS.forgotPassword, { email } as ForgotPasswordRequest);
    return response.data.data;
  },

  verifyOtp: async (email: string, otpCode: string): Promise<string> => {
    const response = await api.post<ApiResponse<string>>(AUTH_ENDPOINTS.verifyOtp, { email, otpCode } as VerifyOtpRequest);
    return response.data.data;
  },

  resetPassword: async (data: ResetPasswordRequest): Promise<string> => {
    const response = await api.post<ApiResponse<string>>(AUTH_ENDPOINTS.resetPassword, data);
    return response.data.data;
  }
};
