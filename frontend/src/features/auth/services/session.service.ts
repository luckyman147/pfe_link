import type { AuthResponse, User } from '../types/auth.types';

export const sessionService = {
  setSession: (data: AuthResponse) => {
    const token = data.token || data.accessToken;
    const refreshToken = data.refreshToken;
    const userId = data.id || data.userId;

    if (token) {
      localStorage.setItem('accessToken', token);
      if (refreshToken) localStorage.setItem('refreshToken', refreshToken);
      localStorage.setItem('user', JSON.stringify({
        id: userId,
        email: data.email,
        fullName: data.fullName,
        role: data.role,
        status: data.status
      }));
    }
  },

  getCurrentUser: (): User | null => {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  },

  isAuthenticated: (): boolean => !!localStorage.getItem('accessToken'),
  getAccessToken: (): string | null => localStorage.getItem('accessToken'),
  
  clearSession: () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
  }
};
