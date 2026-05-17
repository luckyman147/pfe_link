import { Link } from 'react-router-dom';
import { AuthLayout } from '@/features/auth/components';
import { registrationService } from '@/features/auth/services/registration.service';
import { useState } from 'react';

export const MustVerifyEmail = () => {
  const [isResending, setIsResending] = useState(false);
  const [resendMessage, setResendMessage] = useState<{ type: 'success' | 'error'; text: string } | null>(null);

  const handleResendEmail = async () => {
    setIsResending(true);
    try {
      // Note: This would need the user's email from auth context or navigation state
      setResendMessage({ type: 'success', text: 'Verification link sent! Check your inbox.' });
      setTimeout(() => setResendMessage(null), 5000);
    } catch (error) {
      setResendMessage({ type: 'error', text: 'Failed to resend email. Please try again.' });
    } finally {
      setIsResending(false);
    }
  };

  return (
    <AuthLayout
      icon="mail"
      imageAlt="Email Verification"
      imageSrc="https://images.unsplash.com/photo-1557821552-17105176677c?auto=format&fit=crop&q=80&w=1000"
      subtitle="We've sent a verification link to your email address. Click the link to activate your account and get started."
      title="Verify Your Email"
    >
      <div className="flex flex-col gap-6">
        {/* Verification Steps */}
        <div className="space-y-4 bg-stitch-surface-container-low/50 p-6 rounded-2xl border border-stitch-outline-variant/20">
          <div className="flex items-start gap-4">
            <div className="w-8 h-8 rounded-full bg-stitch-primary text-stitch-on-primary flex items-center justify-center font-bold text-sm flex-shrink-0">1</div>
            <div>
              <p className="font-semibold text-stitch-on-surface text-sm">Check your email inbox</p>
              <p className="text-xs text-stitch-on-surface-variant mt-1">Look for a message from HeySir PFE-Link</p>
            </div>
          </div>
          <div className="flex items-start gap-4">
            <div className="w-8 h-8 rounded-full bg-stitch-primary text-stitch-on-primary flex items-center justify-center font-bold text-sm flex-shrink-0">2</div>
            <div>
              <p className="font-semibold text-stitch-on-surface text-sm">Click the verification link</p>
              <p className="text-xs text-stitch-on-surface-variant mt-1">The link will confirm your email address</p>
            </div>
          </div>
          <div className="flex items-start gap-4">
            <div className="w-8 h-8 rounded-full bg-stitch-primary text-stitch-on-primary flex items-center justify-center font-bold text-sm flex-shrink-0">3</div>
            <div>
              <p className="font-semibold text-stitch-on-surface text-sm">Return to sign in</p>
              <p className="text-xs text-stitch-on-surface-variant mt-1">You can now log in with your credentials</p>
            </div>
          </div>
        </div>

        {/* Check for Email Button */}
        <button
          onClick={() => window.location.reload()}
          className="w-full bg-stitch-primary hover:bg-stitch-primary-container text-stitch-on-primary py-4 rounded-2xl font-bold shadow-lg shadow-stitch-primary/30 transition-all active:scale-[0.98] flex items-center justify-center gap-2 group"
        >
          I've Verified My Email
          <span className="material-symbols-outlined text-[20px] group-hover:rotate-12 transition-transform">mail_lock</span>
        </button>

        {/* Resend Email Section */}
        <div className="p-6 rounded-2xl bg-stitch-surface-container-low/30 border border-stitch-outline-variant/20">
          <p className="text-sm font-semibold text-stitch-on-surface mb-3">Didn't receive the email?</p>
          <p className="text-xs text-stitch-on-surface-variant mb-4">Check your spam folder or request a new verification link.</p>
          <button
            disabled={isResending}
            onClick={handleResendEmail}
            className="w-full px-4 py-3 rounded-xl text-stitch-primary font-bold border border-stitch-primary hover:bg-stitch-primary/5 transition-all disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
          >
            {isResending ? (
              <>
                <div className="w-4 h-4 border-2 border-stitch-primary/30 border-t-stitch-primary rounded-full animate-spin" />
                Sending...
              </>
            ) : (
              <>
                <span className="material-symbols-outlined text-[20px]">mail_outline</span>
                Resend Verification Link
              </>
            )}
          </button>

          {/* Status Messages */}
          {resendMessage && (
            <div className={`mt-4 p-3 rounded-xl text-sm font-medium flex items-center gap-2 ${
              resendMessage.type === 'success'
                ? 'bg-green-50 text-green-700 border border-green-200'
                : 'bg-stitch-error-container/10 text-stitch-error border border-stitch-error/20'
            }`}>
              <span className="material-symbols-outlined text-[20px]">
                {resendMessage.type === 'success' ? 'check_circle' : 'warning'}
              </span>
              {resendMessage.text}
            </div>
          )}
        </div>

        {/* Back to Login Link */}
        <div className="text-center pt-4 border-t border-stitch-outline-variant/20">
          <Link
            to="/login"
            className="inline-flex items-center gap-2 text-sm font-bold text-stitch-primary hover:text-stitch-primary-container transition-all group px-4 py-2 rounded-full hover:bg-stitch-primary/5"
          >
            <span className="material-symbols-outlined text-[18px] group-hover:-translate-x-1 transition-transform">arrow_back</span>
            Back to Sign In
          </Link>
        </div>
      </div>
    </AuthLayout>
  );
};
