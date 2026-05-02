import { Link } from "react-router-dom";
import { LogIn, LogOut, User as UserIcon } from "lucide-react";
import { useAuth } from "@/features/auth";

interface AuthActionsProps {
  isLandingPage: boolean;
}

export const AuthActions = ({ isLandingPage }: AuthActionsProps) => {
  const { isAuthenticated, logout } = useAuth();

  if (isAuthenticated) {
    return (
      <div className="flex items-center gap-2">
        <Link 
          to="/dashboard"
          className={`hidden sm:flex items-center gap-2 px-4 py-2 text-sm font-medium rounded-full transition-all ${
            isLandingPage
              ? 'bg-white/10 text-white hover:bg-white/20 backdrop-blur-sm'
              : 'bg-primary-50 text-primary-600 hover:bg-primary-100'
          }`}
        >
          <UserIcon className="w-4 h-4" />
          Dashboard
        </Link>
        <button 
          onClick={logout} 
          className={`flex items-center gap-2 px-4 py-2 text-sm font-medium rounded-full transition-all ${
            isLandingPage
              ? 'bg-red-500/20 text-white hover:bg-red-500/30 backdrop-blur-sm'
              : 'bg-red-50 text-red-600 hover:bg-red-100'
          }`}
        >
          <LogOut className="w-4 h-4" />
          <span className="hidden sm:inline">Logout</span>
        </button>
      </div>
    );
  }

  return (
    <div className="flex items-center gap-2">
      <Link 
        to="/login"
        className={`flex items-center gap-2 px-5 py-2.5 text-sm font-semibold rounded-full transition-all ${
          isLandingPage
            ? 'bg-white text-primary-600 hover:bg-white/90 shadow-lg shadow-black/10'
            : 'bg-primary-500 text-white hover:bg-primary-600 shadow-lg shadow-primary-500/30'
        }`}
      >
        <LogIn className="w-4 h-4" />
        Sign In
      </Link>
    </div>
  );
};
