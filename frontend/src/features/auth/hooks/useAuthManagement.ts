import { useState, useEffect, useRef } from 'react';
import { useGoogleReCaptcha } from 'react-google-recaptcha-v3';
import { authService, sessionService, registrationService } from '@/features/auth/services';
import type { 
  User, 
  AuthResponse,
  LoginRequest,
  StudentRegistrationRequest,
  AdvisorRegistrationRequest
} from '@/features/auth/types/auth.types';

export const useAuthManagement = () => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const initRef = useRef(false);
  const { executeRecaptcha } = useGoogleReCaptcha();

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
    // Recaptcha is optional - continue without it if not configured
    let recaptchaToken: string | undefined;
    if (executeRecaptcha) {
      try {
        recaptchaToken = await executeRecaptcha('login');
      } catch (e) {
        console.warn('Recaptcha failed, continuing without token');
      }
    }
    const response = await authService.login(credentials, recaptchaToken);
    
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
    let recaptchaToken: string | undefined;
    if (executeRecaptcha) {
      try {
        recaptchaToken = await executeRecaptcha('signup_student');
      } catch (e) {
        console.warn('Recaptcha failed, continuing without token');
      }
    }
    const response = await registrationService.registerStudent(data, recaptchaToken);
    if (response.token || response.accessToken) {
      sessionService.setSession(response);
      await fetchUserDetails();
    }
    return response;
  };

  const registerAdvisor = async (data: AdvisorRegistrationRequest): Promise<AuthResponse> => {
    let recaptchaToken: string | undefined;
    if (executeRecaptcha) {
      try {
        recaptchaToken = await executeRecaptcha('signup_advisor');
      } catch (e) {
        console.warn('Recaptcha failed, continuing without token');
      }
    }
    const response = await registrationService.registerAdvisor(data, recaptchaToken);
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
