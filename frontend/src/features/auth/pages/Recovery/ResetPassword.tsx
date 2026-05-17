import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { AuthLayout } from '@/features/auth/components';
import { recoveryService } from '@/features/auth/services/recovery.service';

const schema = z.object({
  email: z.string().email('Valid email is required'),
  otpCode: z.string().length(6, 'OTP must be 6 digits'),
  newPassword: z.string().min(8, 'Password must be at least 8 characters'),
  confirmPassword: z.string(),
}).refine((d) => d.newPassword === d.confirmPassword, {
  message: 'Passwords do not match',
  path: ['confirmPassword'],
});

type ResetPasswordInput = z.infer<typeof schema>;

export const ResetPassword = () => {
  const navigate = useNavigate();
  const form = useForm<ResetPasswordInput>({ resolver: zodResolver(schema) });

  const onSubmit = async (data: ResetPasswordInput) => {
    try {
      await recoveryService.resetPassword({
        email: data.email,
        otpCode: data.otpCode,
        newPassword: data.newPassword,
      });
      navigate('/login');
    } catch (error: any) {
      form.setError('root', { message: error?.response?.data?.message || 'Reset password failed' });
    }
  };

  return (
    <AuthLayout
      icon="password"
      imageAlt="Reset Password"
      imageSrc="https://images.unsplash.com/photo-1558449028-b53a39d100fc?auto=format&fit=crop&q=80&w=1000"
      subtitle="Enter your email, OTP code, and your new password."
      title="Reset Password"
    >
      <form onSubmit={form.handleSubmit(onSubmit)} className="flex flex-col gap-4">
        <input {...form.register('email')} placeholder="name@university.edu" className="w-full px-4 py-3 border rounded-xl" />
        <input {...form.register('otpCode')} placeholder="6-digit OTP" className="w-full px-4 py-3 border rounded-xl" />
        <input {...form.register('newPassword')} type="password" placeholder="New password" className="w-full px-4 py-3 border rounded-xl" />
        <input {...form.register('confirmPassword')} type="password" placeholder="Confirm password" className="w-full px-4 py-3 border rounded-xl" />
        {form.formState.errors.root && <p className="text-xs text-red-600">{form.formState.errors.root.message as string}</p>}
        <button disabled={form.formState.isSubmitting} className="w-full bg-stitch-primary text-white py-3 rounded-xl font-bold">
          {form.formState.isSubmitting ? 'Resetting...' : 'Reset Password'}
        </button>
      </form>
    </AuthLayout>
  );
};
