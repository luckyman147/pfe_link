import api from '@/shared/services/api';
import type { 
  AuthResponse, 
  StudentRegistrationRequest, 
  AdvisorRegistrationRequest 
} from '../types/auth.types';
import { sessionService } from './session.service';
import type { ApiResponse } from '@/shared/types/api';

export const registrationService = {
  registerStudent: async (data: StudentRegistrationRequest, recaptchaToken?: string): Promise<AuthResponse> => {
    const response = await api.post<ApiResponse<AuthResponse>>('/api/auth/signup/student', data, {
      headers: recaptchaToken ? { 'X-Recaptcha-Token': recaptchaToken } : {}
    });
    const resData = response.data.data;
    if (resData.token || resData.accessToken) {
      sessionService.setSession(resData);
    }
    return resData;
  },


  registerAdvisor: async (data: AdvisorRegistrationRequest, recaptchaToken?: string): Promise<AuthResponse> => {
    const response = await api.post<ApiResponse<AuthResponse>>('/api/auth/signup/advisor', data, {
      headers: recaptchaToken ? { 'X-Recaptcha-Token': recaptchaToken } : {}
    });
    const resData = response.data.data;
    if (resData.token || resData.accessToken) {
      sessionService.setSession(resData);
    }
    return resData;
  },

  verifyEmail: async (token: string): Promise<string> => {
    const response = await api.get<ApiResponse<string>>('/api/auth/verify-email', { params: { token } });
    return response.data.data;
  },

  resendVerification: async (email: string): Promise<string> => {
    const response = await api.post<ApiResponse<string>>('/api/auth/resend-verification', null, { params: { email } });
    return response.data.data;
  },

  verifyOtp: async (email: string, otpCode: string): Promise<AuthResponse> => {
    const response = await api.post<ApiResponse<AuthResponse>>('/api/auth/verify-otp', { email, otpCode });
    return response.data.data;
  }
};
