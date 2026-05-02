import { createContext, useContext } from 'react';
import type { ReactNode } from 'react';
import { useAuthManagement } from '../hooks/useAuthManagement';
import type { 
  User, 
  AuthResponse,
  LoginRequest,
  StudentRegistrationRequest,
  AdvisorRegistrationRequest
} from '@/features/auth/types/auth.types';

interface AuthContextType {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (credentials: LoginRequest) => Promise<AuthResponse>;
  logout: () => void;
  registerStudent: (data: StudentRegistrationRequest) => Promise<AuthResponse>;
  registerAdvisor: (data: AdvisorRegistrationRequest) => Promise<AuthResponse>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const auth = useAuthManagement();

  return (
    <AuthContext.Provider value={{ 
      ...auth, 
      isAuthenticated: !!auth.user 
    }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
