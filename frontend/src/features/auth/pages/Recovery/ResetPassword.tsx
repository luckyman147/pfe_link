import { useNavigate, useLocation } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { AuthLayout } from '@/features/auth/components';
import { recoveryService } from '@/features/auth/services/recovery.service';

const schema = z.object({
  email: z.string().email('Valid email is required'),
  otpCode: z.string().length(6, 'OTP must be 6 digits'),
  newPassword: z.string().min(8, 'Password must be at least 8 characters')
    .regex(/[A-Z]/, 'Must contain uppercase letter')
    .regex(/[0-9]/, 'Must contain a number')
    .regex(/[!@#$%^&*]/, 'Must contain a special character'),
  confirmPassword: z.string(),
}).refine((d) => d.newPassword === d.confirmPassword, {
  message: 'Passwords do not match',
  path: ['confirmPassword'],
});

type ResetPasswordInput = z.infer<typeof schema>;

export const ResetPassword = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const email = location.state?.email || '';

  const form = useForm<ResetPasswordInput>({
    resolver: zodResolver(schema),
    defaultValues: {
      email: email,
    }
  });

  const onSubmit = async (data: ResetPasswordInput) => {
    try {
      await recoveryService.resetPassword({
        email: data.email,
        otpCode: data.otpCode,
        newPassword: data.newPassword,
      });
      navigate('/login', { replace: true });
    } catch (error: any) {
      form.setError('root', { message: error?.response?.data?.message || 'Reset password failed. Please try again.' });
    }
  };

  const { formState: { errors, isSubmitting } } = form;

  return (
    <AuthLayout
      icon="password"
      imageAlt="Reset Password"
      imageSrc="https://images.unsplash.com/photo-1558449028-b53a39d100fc?auto=format&fit=crop&q=80&w=1000"
      subtitle="Create a strong new password to secure your account. Your password will be updated immediately."
      title="Create New Password"
    >
      <form onSubmit={form.handleSubmit(onSubmit)} className="flex flex-col gap-6">
        {/* Email Field */}
        <div className="flex flex-col gap-3">
          <label className="text-sm font-bold text-stitch-on-surface">Email Address</label>
          <input
            {...form.register('email')}
            placeholder="name@university.edu"
            type="email"
            disabled
            className="w-full px-4 py-3 border border-stitch-outline-variant rounded-2xl bg-stitch-surface-container-low text-stitch-on-surface placeholder-stitch-on-surface-variant/50 focus:outline-none focus:border-stitch-primary disabled:opacity-50"
          />
          {errors.email && <span className="text-xs text-stitch-error font-medium">{errors.email.message}</span>}
        </div>

        {/* OTP Code Field */}
        <div className="flex flex-col gap-3">
          <label className="text-sm font-bold text-stitch-on-surface">Verification Code</label>
          <input
            {...form.register('otpCode')}
            placeholder="000000"
            maxLength={6}
            className="w-full px-4 py-3 border border-stitch-outline-variant rounded-2xl bg-white text-stitch-on-surface placeholder-stitch-on-surface-variant/50 focus:outline-none focus:border-stitch-primary focus:ring-2 focus:ring-stitch-primary/10 font-mono text-center text-lg tracking-widest"
          />
          {errors.otpCode && <span className="text-xs text-stitch-error font-medium">{errors.otpCode.message}</span>}
        </div>

        {/* New Password Field */}
        <div className="flex flex-col gap-3">
          <label className="text-sm font-bold text-stitch-on-surface">New Password</label>
          <input
            {...form.register('newPassword')}
            type="password"
            placeholder="Min 8 chars with uppercase, number, and symbol"
            className="w-full px-4 py-3 border border-stitch-outline-variant rounded-2xl bg-white text-stitch-on-surface placeholder-stitch-on-surface-variant/50 focus:outline-none focus:border-stitch-primary focus:ring-2 focus:ring-stitch-primary/10"
          />
          {errors.newPassword && <span className="text-xs text-stitch-error font-medium">{errors.newPassword.message}</span>}
          <div className="text-xs text-stitch-on-surface-variant font-medium space-y-1 mt-2">
            <p>Password must contain:</p>
            <ul className="space-y-1 ml-2">
              <li>• At least 8 characters</li>
              <li>• One uppercase letter</li>
              <li>• One number</li>
              <li>• One special character (!@#$%^&*)</li>
            </ul>
          </div>
        </div>

        {/* Confirm Password Field */}
        <div className="flex flex-col gap-3">
          <label className="text-sm font-bold text-stitch-on-surface">Confirm Password</label>
          <input
            {...form.register('confirmPassword')}
            type="password"
            placeholder="Re-enter your new password"
            className="w-full px-4 py-3 border border-stitch-outline-variant rounded-2xl bg-white text-stitch-on-surface placeholder-stitch-on-surface-variant/50 focus:outline-none focus:border-stitch-primary focus:ring-2 focus:ring-stitch-primary/10"
          />
          {errors.confirmPassword && <span className="text-xs text-stitch-error font-medium">{errors.confirmPassword.message}</span>}
        </div>

        {/* Error Alert */}
        {errors.root && (
          <div className="flex items-start gap-3 bg-stitch-error-container/10 text-stitch-error p-4 rounded-2xl border border-stitch-error/20 animate-in shake duration-500">
            <span className="material-symbols-outlined text-[20px] flex-shrink-0 mt-0.5">warning</span>
            <div>
              <p className="text-sm font-bold">{errors.root.message}</p>
            </div>
          </div>
        )}

        {/* Submit Button */}
        <button
          disabled={isSubmitting}
          type="submit"
          className="w-full bg-stitch-primary hover:bg-stitch-primary-container text-stitch-on-primary py-4 rounded-2xl font-bold shadow-lg shadow-stitch-primary/30 transition-all active:scale-[0.98] disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2 group mt-4"
        >
          {isSubmitting ? (
            <>
              <div className="w-5 h-5 border-2 border-stitch-on-primary/30 border-t-stitch-on-primary rounded-full animate-spin" />
              Resetting Password...
            </>
          ) : (
            <>
              Reset Password
              <span className="material-symbols-outlined text-[20px] group-hover:rotate-12 transition-transform">check_circle</span>
            </>
          )}
        </button>
      </form>
    </AuthLayout>
  );
};
