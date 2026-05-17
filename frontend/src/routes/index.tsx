import { Routes, Route, Navigate } from "react-router-dom";
import { 
    UserTypeSelection, 
    StudentSignup, 
    AdvisorSignup, 
    Login, 
    ForgotPassword,
    ResetPassword,
    UserRole,
    useAuth,
    MustVerifyEmail,
    VerifyEmailConfirm,
    OTPVerification,
    type User
} from "@/features/auth";
import { AdminDashboard, CreateFacultyPage } from "@/features/admin";
import { StudentDashboard } from "@/features/student";
import { AdvisorDashboard } from "@/features/advisor";
import { LandingPage } from "@/features/landing";
import { ProtectedLayout } from "@/shared/components/layout/ProtectedLayout";
import { AdvisorSelection } from "@/features/academic";

interface DashboardRedirectProps {
    isAuthenticated: boolean;
    isLoading: boolean;
    user: User | null;
}

const DashboardRedirect: React.FC<DashboardRedirectProps> = ({ isAuthenticated, isLoading, user }) => {
    if (isLoading) return <LoadingSpinner />;
    if (!isAuthenticated) return <Navigate to="/login" />;
    
    switch(user?.role) {
        case UserRole.ADMIN: return <Navigate to="/admin" />;
        case UserRole.STUDENT: return <Navigate to="/student" />;
        case UserRole.ADVISOR: return <Navigate to="/advisor" />;
        default: return <Navigate to="/login" />;
    }
};

export const AppRoutes = () => {
    const { isAuthenticated, isLoading, user } = useAuth();
    
    return (
        <Routes>
            {/* Landing Page */}
            <Route path="/" element={<LandingPage />} />
            <Route path="/landing" element={<LandingPage />} />
            
            {/* Public Auth Routes */}
            <Route path="/auth/signup" element={<UserTypeSelection />} />
            <Route path="/auth/signup/student" element={<StudentSignup />} />
            <Route path="/auth/signup/advisor" element={<AdvisorSignup />} />
            <Route path="/login" element={<Login />} />
            <Route path="/auth/forgot-password" element={<ForgotPassword />} />
            <Route path="/auth/reset-password" element={<ResetPassword />} />
            <Route path="/auth/verify-email" element={<MustVerifyEmail />} />
            <Route path="/auth/verify-email/confirm" element={<VerifyEmailConfirm />} />
            <Route path="/auth/verify-otp" element={<OTPVerification />} />
            
            {/* Dashboard Redirect */}
            <Route path="/dashboard" element={
                <DashboardRedirect 
                    isAuthenticated={isAuthenticated} 
                    isLoading={isLoading} 
                    user={user} 
                />
            } />

            {/* Protected Routes */}
            <Route path="/admin" element={
                <ProtectedLayout allowedRoles={[UserRole.ADMIN]}>
                    <AdminDashboard />
                </ProtectedLayout>
            } />
            <Route path="/admin/faculties/new" element={
                <ProtectedLayout allowedRoles={[UserRole.ADMIN]}>
                    <CreateFacultyPage />
                </ProtectedLayout>
            } />
            <Route path="/student" element={
                <ProtectedLayout allowedRoles={[UserRole.STUDENT]}>
                    <StudentDashboard />
                </ProtectedLayout>
            } />
            <Route path="/student/advisor-selection" element={
                <ProtectedLayout allowedRoles={[UserRole.STUDENT]}>
                    <AdvisorSelection />
                </ProtectedLayout>
            } />
            <Route path="/advisor" element={
                <ProtectedLayout allowedRoles={[UserRole.ADVISOR]}>
                    <AdvisorDashboard />
                </ProtectedLayout>
            } />
        </Routes>
    );
};

const LoadingSpinner = () => (
    <div className="min-h-screen flex items-center justify-center bg-linear-to-br from-primary-50 to-sky-50">
        <div className="flex flex-col items-center gap-4">
            <div className="w-12 h-12 border-4 border-primary-200 border-t-primary-500 rounded-full animate-spin" />
            <p className="text-navy-600 font-medium">Loading...</p>
        </div>
    </div>
);
