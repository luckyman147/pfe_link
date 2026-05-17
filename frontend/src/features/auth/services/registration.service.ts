import api from '@/shared/services/api';
import type {
  AuthResponse,
  StudentRegistrationRequest,
  AdvisorRegistrationRequest
} from '../types/auth.types';
import { sessionService } from './session.service';
import type { ApiResponse } from '@/shared/types/api';
import { AUTH_ENDPOINTS } from '@/config/endpoints';

const persistIfTokenized = (data: AuthResponse) => {
  if (data.token || data.accessToken) sessionService.setSession(data);
  return data;
};

export const registrationService = {
  registerStudent: async (data: StudentRegistrationRequest): Promise<AuthResponse> => {
    const response = await api.post<ApiResponse<AuthResponse>>(
      AUTH_ENDPOINTS.signupStudent, data
    );
    return persistIfTokenized(response.data.data);
  },

  registerAdvisor: async (data: AdvisorRegistrationRequest): Promise<AuthResponse> => {
    const response = await api.post<ApiResponse<AuthResponse>>(
      AUTH_ENDPOINTS.signupAdvisor, data
    );
    return persistIfTokenized(response.data.data);
  },

  verifyEmail: async (token: string): Promise<boolean> => {
    const response = await api.post<ApiResponse<boolean>>(AUTH_ENDPOINTS.verifyEmail, null, {
      params: { token },
    });
    return response.data.data;
  },

  resendVerification: async (email: string): Promise<string> => {
    const response = await api.post<ApiResponse<string>>(AUTH_ENDPOINTS.resendVerify, null, { params: { email } });
    return response.data.data;
  },

  verifyOtp: async (email: string, otpCode: string): Promise<AuthResponse> => {
    const response = await api.post<ApiResponse<AuthResponse>>(AUTH_ENDPOINTS.verifyOtp, { email, otpCode });
    return response.data.data;
  }
};
