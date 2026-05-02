import { useState, useEffect } from 'react';
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
  const { executeRecaptcha } = useGoogleReCaptcha();

  const fetchUserDetails = async () => {
    try {
      const userDetails = await authService.getMe();
      setUser(userDetails);
      return userDetails;
    } catch (error) {
      console.error('Failed to fetch user details:', error);
      return null;
    }
  };

  useEffect(() => {
    const initializeAuth = async () => {
      const accessToken = sessionService.getAccessToken();
      if (accessToken) {
        const details = await fetchUserDetails();
        if (!details && localStorage.getItem('refreshToken')) {
          try {
            await authService.refresh();
            await fetchUserDetails();
          } catch {
            authService.logout();
          }
        } else if (!details) {
          authService.logout();
        }
      }
      setIsLoading(false);
    };
    initializeAuth();
  }, []);

  const login = async (credentials: LoginRequest): Promise<AuthResponse> => {
    if (!executeRecaptcha) throw new Error('ReCaptcha not ready');
    const token = await executeRecaptcha('login');
    const response = await authService.login(credentials, token);
    
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
    if (!executeRecaptcha) throw new Error('ReCaptcha not ready');
    const token = await executeRecaptcha('signup_student');
    const response = await registrationService.registerStudent(data, token);
    if (response.token || response.accessToken) {
      sessionService.setSession(response);
      await fetchUserDetails();
    }
    return response;
  };

  const registerAdvisor = async (data: AdvisorRegistrationRequest): Promise<AuthResponse> => {
    if (!executeRecaptcha) throw new Error('ReCaptcha not ready');
    const token = await executeRecaptcha('signup_advisor');
    const response = await registrationService.registerAdvisor(data, token);
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
