import { useState, useEffect, useRef } from 'react';
import { authService, sessionService, registrationService } from '@/features/auth/services';
import type {
  User,
  AuthResponse,
  LoginRequest,
  StudentRegistrationRequest,
  AdvisorRegistrationRequest
} from '@/features/auth/types/auth.types';

declare global {
  interface Window {
    turnstile?: {
      getResponse(): string;
      reset(): void;
      remove(): void;
      render(element: HTMLElement, options: Record<string, unknown>): void;
      isExpired(): boolean;
    };
  }
}

const getTurnstileToken = (): string | undefined => {
  if (typeof window !== 'undefined' && window.turnstile) {
    return window.turnstile.getResponse() || undefined;
  }
  return undefined;
};

export const useAuthManagement = () => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const initRef = useRef(false);

  const fetchUserDetails = async () => {
    try {
      const userDetails = await authService.getMe();
      setUser(userDetails);
      return userDetails;
    } catch (error: any) {
      // Only log, don't set user state here
      if (error?.response?.status !== 401) {
        console.error('Failed to fetch user details:', error);
      }
      return null;
    }
  };

  useEffect(() => {
    if (initRef.current) return;
    initRef.current = true;

    const initializeAuth = async () => {
      try {
        const accessToken = sessionService.getAccessToken();
        if (!accessToken) {
          setIsLoading(false);
          return;
        }

        const details = await fetchUserDetails();
        
        if (!details) {
          // Try refresh token only if we have one
          const refreshToken = localStorage.getItem('refreshToken');
          if (refreshToken) {
            try {
              await authService.refresh();
              await fetchUserDetails();
            } catch {
              // Refresh failed, clear everything
              authService.logout();
              setUser(null);
            }
          } else {
            authService.logout();
            setUser(null);
          }
        }
      } catch (error) {
        console.error('Auth initialization error:', error);
      } finally {
        setIsLoading(false);
      }
    };

    initializeAuth();
  }, []);

  const login = async (credentials: LoginRequest): Promise<AuthResponse> => {
    const turnstileToken = getTurnstileToken();
    const response = await authService.login(credentials, turnstileToken);

    if (response.token || response.accessToken) {
      sessionService.setSession(response);
      await fetchUserDetails();
    }
    return response;
  };

  const logout = () => {
    authService.logout();
    setUser(null);
  };

  const registerStudent = async (data: StudentRegistrationRequest): Promise<AuthResponse> => {
    const turnstileToken = getTurnstileToken();
    const response = await registrationService.registerStudent(data, turnstileToken);
    if (response.token || response.accessToken) {
      sessionService.setSession(response);
      await fetchUserDetails();
    }
    return response;
  };

  const registerAdvisor = async (data: AdvisorRegistrationRequest): Promise<AuthResponse> => {
    const turnstileToken = getTurnstileToken();
    const response = await registrationService.registerAdvisor(data, turnstileToken);
    if (response.token || response.accessToken) {
      sessionService.setSession(response);
      await fetchUserDetails();
    }
    return response;
  };

  return {
    user,
    isLoading,
    login,
    logout,
    registerStudent,
    registerAdvisor
  };
};
