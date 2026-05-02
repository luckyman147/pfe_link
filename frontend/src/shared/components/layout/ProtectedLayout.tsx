import { type ReactNode } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth, UserRole } from '@/features/auth';

interface ProtectedLayoutProps {
  children: ReactNode;
  allowedRoles?: UserRole[];
}

export const ProtectedLayout = ({ children, allowedRoles }: ProtectedLayoutProps) => {
  const { user, isAuthenticated, isLoading } = useAuth();
  const location = useLocation();

  if (isLoading) {
    return <div className="h-screen flex items-center justify-center">Loading context...</div>;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  if (allowedRoles && user && !allowedRoles.includes(user.role)) {
    // Redirect to appropriate dashboard based on role
    // Or show unauthorized page
    if (user.role === UserRole.ADMIN) return <Navigate to="/admin" replace />;
    if (user.role === UserRole.STUDENT) return <Navigate to="/student" replace />;
    if (user.role === UserRole.ADVISOR) return <Navigate to="/advisor" replace />; // or TEACHER
    return <Navigate to="/" replace />;
  }

  return <>{children}</>;
};
