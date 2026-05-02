import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useOTPVerification } from '@/features/auth/hooks';
import { OTPInput } from '@/features/auth/components';
import { AuthLayout } from '@/features/auth/components';

export const OTPVerification: React.FC = () => {
  const { handleSubmit, setValue, watch, errors, timer, formatTime, onVerify, resendCode } = useOTPVerification();
  const location = useLocation();
  const email = location.state?.email || '';
  const otp = watch('otp');

  return (
    <AuthLayout
      title="Verify Identity"
      subtitle="We've sent a 6-digit security code to your academic email. Please enter it below to authorize your session."
      imageSrc="https://images.unsplash.com/photo-1614064641938-3bbee52942c7?auto=format&fit=crop&q=80&w=1000"
      imageAlt="Secure Access"
      icon="verified_user"
    >
      <div className="flex flex-col gap-3 mb-10 text-center sm:text-left">
        <h2 className="text-4xl font-extrabold tracking-tight text-stitch-on-surface leading-tight">
          Verify Identity
        </h2>
        <p className="text-stitch-on-surface-variant text-lg font-medium leading-relaxed">
          We've sent a 6-digit security code to your academic email.
        </p>
      </div>

      <form 
        onSubmit={handleSubmit((data) => onVerify(data, email))} 
        className="flex flex-col gap-8"
      >
        <div className="flex justify-center py-8 bg-white rounded-3xl border border-stitch-outline-variant/30 shadow-sm relative overflow-hidden group">
          <div className="absolute inset-0 bg-stitch-primary/5 opacity-0 group-hover:opacity-100 transition-opacity" />
          <OTPInput error={!!errors.otp} onChange={(val) => setValue('otp', val)} value={otp} />
        </div>

        {errors.otp && (
          <div className="flex items-center gap-3 text-stitch-error bg-stitch-error-container/10 p-4 rounded-2xl border border-stitch-error/20 animate-in shake duration-500">
            <span className="material-symbols-outlined text-[20px]">warning</span>
            <span className="text-sm font-bold">{errors.otp.message}</span>
          </div>
        )}

        <button 
          className="w-full bg-stitch-primary hover:bg-stitch-primary-container text-stitch-on-primary py-4 rounded-2xl font-bold shadow-xl shadow-stitch-primary/20 transition-all active:scale-[0.98] flex items-center justify-center gap-2 group"
          type="submit"
        >
          Authorize Session
          <span className="material-symbols-outlined text-[20px] group-hover:rotate-12 transition-transform">verified</span>
        </button>
      </form>

      <div className="mt-12 p-8 rounded-[32px] bg-stitch-surface-container-low/50 border border-stitch-outline-variant/20 flex flex-col items-center gap-4">
        <div className="w-12 h-12 rounded-full bg-white flex items-center justify-center shadow-sm">
          <span className="material-symbols-outlined text-stitch-primary text-[24px]">mail</span>
        </div>
        <div className="text-center">
          <p className="text-sm text-stitch-on-surface-variant font-bold mb-1">Didn't receive the code?</p>
          <p className="text-xs text-stitch-on-surface-variant/60 font-medium max-w-[200px]">Check your spam folder or request a new one.</p>
        </div>
        <button 
          disabled={timer > 0}
          onClick={() => resendCode(email)}
          className={`px-8 py-3 rounded-full text-sm font-bold transition-all duration-300 ${
            timer > 0 
              ? 'bg-stitch-outline-variant/20 text-stitch-outline cursor-not-allowed' 
              : 'bg-white text-stitch-primary hover:bg-stitch-primary hover:text-white shadow-md'
          }`}
        >
          {timer > 0 ? `Resend in ${formatTime(timer)}` : 'Request New Code'}
        </button>
      </div>

      <div className="mt-10 text-center">
        <Link 
          className="inline-flex items-center gap-2 text-sm font-bold text-stitch-primary hover:text-stitch-primary-container transition-all group px-4 py-2 rounded-full hover:bg-stitch-primary/5"
          to="/auth/login"
        >
          <span className="material-symbols-outlined text-[18px] group-hover:-translate-x-1 transition-transform">arrow_back</span>
          Return to Sign In
        </Link>
      </div>
    </AuthLayout>
  );
};
