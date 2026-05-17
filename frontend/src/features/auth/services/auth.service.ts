import api from '@/shared/services/api';
import type { AuthResponse, RefreshTokenResponse, LoginRequest, User } from '../types/auth.types';
import { sessionService } from './session.service';
import type { ApiResponse } from '@/shared/types/api';
import { AUTH_ENDPOINTS } from '@/config/endpoints';

export const authService = {
  login: async (credentials: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post<ApiResponse<AuthResponse>>(AUTH_ENDPOINTS.login, credentials);
    const data = response.data.data;
    const token = data.token || data.accessToken;
    if (token) {
      sessionService.setSession(data);
    }
    return data;
  },

  getMe: async (): Promise<User> => {
    const response = await api.get<ApiResponse<User>>(AUTH_ENDPOINTS.me);
    return response.data.data;
  },

  refresh: async (): Promise<RefreshTokenResponse> => {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) throw new Error('No refresh token available');
    
    const response = await api.post<ApiResponse<RefreshTokenResponse>>(AUTH_ENDPOINTS.refresh, { refreshToken });
    const data = response.data.data;
    
    if (data.accessToken) {
      localStorage.setItem('accessToken', data.accessToken);
      if (data.refreshToken) {
        localStorage.setItem('refreshToken', data.refreshToken);
      }
    }
    return data;
  },

  logout: async () => {
    try {
      await api.post(AUTH_ENDPOINTS.logout);
    } catch (e) {
      console.error('Logout failed on server:', e);
    } finally {
      sessionService.clearSession();
    }
  }
};
