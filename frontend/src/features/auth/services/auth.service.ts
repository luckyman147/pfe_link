import api from '@/shared/services/api';
import type { AuthResponse, LoginRequest, User } from '../types/auth.types';
import { sessionService } from './session.service';
import type { ApiResponse } from '@/shared/types/api';

/**
 * Authentication Service
 * Handles core auth operations: login, logout, and token refresh
 */
export const authService = {
  login: async (credentials: LoginRequest, recaptchaToken?: string): Promise<AuthResponse> => {
    const response = await api.post<ApiResponse<AuthResponse>>('/api/auth/login', credentials, {
      headers: recaptchaToken ? { 'X-Recaptcha-Token': recaptchaToken } : {}
    });
    const data = response.data.data;
    const token = data.token || data.accessToken;
    
    if (token) {
      sessionService.setSession(data);
    }
    return data;
  },

  getMe: async (): Promise<User> => {
    const response = await api.get<ApiResponse<User>>('/api/auth/me');
    return response.data.data;
  },

  refresh: async (): Promise<AuthResponse> => {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) throw new Error('No refresh token available');

    const response = await api.post<ApiResponse<AuthResponse>>('/api/auth/refresh', { refreshToken });
    const data = response.data.data;
    
    if (data.token || data.accessToken) {
      localStorage.setItem('accessToken', data.token || data.accessToken || '');
      if (data.refreshToken) {
        localStorage.setItem('refreshToken', data.refreshToken);
      }
    }
    return data;
  },

  logout: async () => {
    try {
      await api.post('/api/auth/logout');
    } catch (e) {
      console.error('Logout failed on server:', e);
    } finally {
      sessionService.clearSession();
    }
  }
};
