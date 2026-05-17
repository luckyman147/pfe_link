import React, { memo, useMemo } from 'react';
import { Link } from 'react-router-dom';
import { useLogin } from '../../hooks';

// Memoized icon components to prevent re-renders
const EmailIcon = memo(() => (
  <span className="material-symbols-outlined text-[18px]" aria-hidden="true">alternate_email</span>
));
EmailIcon.displayName = 'EmailIcon';

const LockIcon = memo(() => (
  <span className="material-symbols-outlined text-[18px]" aria-hidden="true">lock</span>
));
LockIcon.displayName = 'LockIcon';

const ErrorIcon = memo(() => (
  <span className="material-symbols-outlined text-[14px]" aria-hidden="true">error</span>
));
ErrorIcon.displayName = 'ErrorIcon';

const LoginFormComponent: React.FC = () => {
  const { register, handleSubmit, errors, isSubmitting, onSubmit } = useLogin();
  const [showPassword, setShowPassword] = React.useState(false);

  // Memoize error messages to prevent recalculation
  const emailErrorContent = useMemo(() =>
    errors.email ? (
      <div className="flex items-center gap-1.5 text-xs text-stitch-error mt-1.5 px-1 font-medium animate-in fade-in slide-in-from-top-1">
        <ErrorIcon />
        {errors.email.message}
      </div>
    ) : null,
    [errors.email]
  );

  const passwordErrorContent = useMemo(() =>
    errors.password ? (
      <div className="flex items-center gap-1.5 text-xs text-stitch-error mt-1.5 px-1 font-medium animate-in fade-in slide-in-from-top-1">
        <ErrorIcon />
        {errors.password.message}
      </div>
    ) : null,
    [errors.password]
  );

  const rootErrorContent = useMemo(() =>
    errors.root ? (
      <div className="p-4 bg-stitch-error-container/10 border border-stitch-error/20 rounded-2xl text-xs text-stitch-error font-medium flex items-center gap-3 animate-in shake duration-500">
        <span className="material-symbols-outlined text-[20px]" aria-hidden="true">report</span>
        {errors.root.message}
      </div>
    ) : null,
    [errors.root]
  );

  return (
    <form className="flex flex-col gap-6" onSubmit={(e) => { e.preventDefault(); handleSubmit(onSubmit)(); }}>
      <div className="flex flex-col gap-2">
        <label className="text-sm font-bold text-stitch-on-surface-variant px-1 flex items-center gap-2" htmlFor="email">
          <EmailIcon />
          Academic Email
        </label>
        <div className="relative group">
          <input
            {...register("email")}
            id="email"
            type="email"
            placeholder="name@university.edu"
            autoComplete="email"
            inputMode="email"
            className={`w-full bg-white border rounded-2xl px-5 py-4 text-sm transition-all duration-300 outline-none shadow-sm
              ${errors.email
                ? 'border-stitch-error focus:border-stitch-error focus:ring-4 focus:ring-stitch-error/10'
                : 'border-stitch-outline-variant/50 focus:border-stitch-primary focus:ring-4 focus:ring-stitch-primary/10 group-hover:border-stitch-primary/50'}`}
          />
        </div>
        {emailErrorContent}
      </div>

      <div className="flex flex-col gap-2">
        <div className="flex justify-between items-center px-1">
          <label className="text-sm font-bold text-stitch-on-surface-variant flex items-center gap-2" htmlFor="password">
            <LockIcon />
            Password
          </label>
          <Link
            to="/auth/forgot-password"
            className="text-xs font-bold text-stitch-primary hover:text-stitch-primary-container transition-colors"
          >
            Forgot?
          </Link>
        </div>
        <div className="relative group">
          <input
            {...register("password")}
            id="password"
            type={showPassword ? "text" : "password"}
            placeholder="••••••••"
            autoComplete="current-password"
            className={`w-full bg-white border rounded-2xl px-5 py-4 text-sm transition-all duration-300 outline-none shadow-sm
              ${errors.password
                ? 'border-stitch-error focus:border-stitch-error focus:ring-4 focus:ring-stitch-error/10'
                : 'border-stitch-outline-variant/50 focus:border-stitch-primary focus:ring-4 focus:ring-stitch-primary/10 group-hover:border-stitch-primary/50'}`}
          />
          <button
            type="button"
            onClick={() => setShowPassword(!showPassword)}
            className="absolute right-4 top-1/2 -translate-y-1/2 text-stitch-on-surface-variant/50 hover:text-stitch-primary transition-colors focus:outline-none"
            aria-label={showPassword ? 'Hide password' : 'Show password'}
          >
            <span className="material-symbols-outlined text-[20px]" aria-hidden="true">
              {showPassword ? 'visibility_off' : 'visibility'}
            </span>
          </button>
        </div>
        {passwordErrorContent}
      </div>

      {rootErrorContent}

      <button
        disabled={isSubmitting}
        type="submit"
        className="w-full mt-4 bg-stitch-primary hover:bg-stitch-primary-container text-stitch-on-primary font-bold py-4 rounded-2xl shadow-xl shadow-stitch-primary/20 transition-all active:scale-[0.98] disabled:opacity-50 disabled:active:scale-100 flex items-center justify-center gap-2 overflow-hidden relative"
      >
        {isSubmitting ? (
          <>
            <span className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
            Verifying Identity...
          </>
        ) : (
          <>
            Sign In to Portal
            <span className="material-symbols-outlined text-[20px]" aria-hidden="true">login</span>
          </>
        )}
      </button>

      <div className="relative my-4">
        <div className="absolute inset-0 flex items-center"><div className="w-full border-t border-stitch-outline-variant/30"></div></div>
        <div className="relative flex justify-center text-[10px] uppercase tracking-widest font-bold"><span className="bg-stitch-surface-container-low px-4 text-stitch-on-surface-variant/40">Secured Institutional Login</span></div>
      </div>

      <button
        type="button"
        className="w-full bg-white border border-stitch-outline-variant/50 hover:bg-stitch-surface-container-low hover:border-stitch-primary/30 text-stitch-on-surface font-bold py-4 rounded-2xl flex items-center justify-center gap-3 transition-all duration-300 shadow-sm"
        title="Sign in using your institutional SSO"
      >
        <svg
          className="w-5 h-5"
          viewBox="0 0 24 24"
          role="img"
          aria-label="Google logo"
          loading="lazy"
        >
          <path d="M22.56 12.25C22.56 11.47 22.49 10.72 22.36 10H12V14.26H17.92C17.66 15.63 16.88 16.8 15.72 17.58V20.34H19.28C21.36 18.42 22.56 15.6 22.56 12.25Z" fill="#4285F4" />
          <path d="M12 23C14.97 23 17.46 22.02 19.28 20.34L15.72 17.58C14.73 18.24 13.48 18.64 12 18.64C9.13 18.64 6.7 16.7 5.84 14.12H2.17V16.96C3.98 20.55 7.69 23 12 23Z" fill="#34A853" />
          <path d="M5.84 14.12C5.62 13.46 5.49 12.75 5.49 12C5.49 11.25 5.62 10.54 5.84 9.88V7.04H2.17C1.43 8.52 1 10.21 1 12C1 13.79 1.43 15.48 2.17 16.96L5.84 14.12Z" fill="#FBBC05" />
          <path d="M12 5.36C13.62 5.36 15.07 5.92 16.21 7.01L19.36 3.86C17.46 2.08 14.97 1 12 1C7.69 1 3.45 3.45 5.36 5.36 5.36 5.36Z" fill="#EA4335" />
        </svg>
        Institutional SSO
      </button>

      <p className="text-[10px] text-center text-stitch-on-surface-variant/40 leading-relaxed">
        This portal uses secure authentication to protect your account.
      </p>
    </form>
  );
};

// Export memoized component to prevent unnecessary re-renders
export const LoginForm = memo(LoginFormComponent);
