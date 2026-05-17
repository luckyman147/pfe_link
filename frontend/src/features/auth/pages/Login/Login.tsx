import React from 'react';
import { Link, Navigate, useLocation } from 'react-router-dom';
import { AuthLayout, LoginForm } from '@/features/auth/components';
import { useAuth } from '@/features/auth';

export const Login: React.FC = () => {
  const { isAuthenticated, isLoading } = useAuth();
  const location = useLocation();

  // Don't redirect until auth is fully loaded to prevent flash
  if (isLoading) {
    return (
      <AuthLayout title="Loading...">
        <div className="flex items-center justify-center p-8">
          <div className="w-8 h-8 border-4 border-stitch-primary border-t-transparent rounded-full animate-spin" />
        </div>
      </AuthLayout>
    );
  }

  if (isAuthenticated) {
    const from = location.state?.from?.pathname || "/dashboard";
    return <Navigate to={from} replace />;
  }

  return (
    <AuthLayout
      title="Welcome to HeySir"
      subtitle="The all-in-one portal for PFE management, student-advisor collaboration, and academic excellence."
      imageSrc="https://lh3.googleusercontent.com/aida-public/AB6AXuCh_hjcMvpDhnzJ5IMHOfjHfy6n6QTlAH2pZ59fQIJMp3vIFepY5axHY6TFPWhwvtznEYbE4r1EO4L1Me2sqi9vugo-YG6WDPE8ImSQKd3bPFRjBYGJQvP_tthIs7yl1ai6QnWJsUOG80AvkIVv5Pd8-w_JmBC5lhBtZK_yj-lBcBdusyuKO6Lu66KSshHKHgYqOwK9ZGRn-V4TeGNcMWQZJSTbbH4EyvuaKOzSQJvnUdjrbJ_vgiZj-lEnJzz5-Z8tHYfieMFphA"
      imageAlt="Academic environment"
    >
      <div className="flex flex-col gap-3 mb-10 text-center sm:text-left">
        <h2 className="text-4xl font-extrabold tracking-tight text-stitch-on-surface leading-tight">
          Sign In
        </h2>
        <p className="text-stitch-on-surface-variant text-lg font-medium leading-relaxed">
          Enter your institutional credentials to access your dashboard.
        </p>
      </div>

      <LoginForm />

      <p className="mt-10 text-center text-sm text-stitch-on-surface-variant font-medium">
        New to the platform?{' '}
        <Link 
          to="/auth/signup" 
          className="text-stitch-primary hover:text-stitch-primary-container font-bold transition-all underline underline-offset-8 decoration-2 decoration-stitch-primary/30 hover:decoration-stitch-primary"
        >
          Request Account
        </Link>
      </p>

      <div className="mt-8 flex items-center justify-center gap-6">
        <Link to="/privacy" className="text-xs text-stitch-on-surface-variant/70 hover:text-stitch-primary transition-colors">
          Privacy Policy
        </Link>
        <div className="w-1 h-1 rounded-full bg-stitch-outline-variant/40" />
        <Link to="/terms" className="text-xs text-stitch-on-surface-variant/70 hover:text-stitch-primary transition-colors">
          Terms of Use
        </Link>
      </div>
    </AuthLayout>
  );
};
