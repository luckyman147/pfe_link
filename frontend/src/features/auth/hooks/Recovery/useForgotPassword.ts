import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { recoveryService } from '@/features/auth/services';

const emailSchema = z.object({
  email: z.string().email("Please enter a valid institutional email"),
});

const otpSchema = z.object({
  otpCode: z.string().length(6, "OTP must be 6 digits"),
});

const passwordSchema = z.object({
  newPassword: z.string().min(8, "Password must be at least 8 characters"),
  confirmPassword: z.string()
}).refine((data) => data.newPassword === data.confirmPassword, {
  message: "Passwords do not match",
  path: ["confirmPassword"],
});

export type EmailInput = z.infer<typeof emailSchema>;
export type OTPInputData = z.infer<typeof otpSchema>;
export type PasswordInput = z.infer<typeof passwordSchema>;
export type ForgotPasswordStep = 1 | 2 | 3;

export const useForgotPassword = () => {
  const [step, setStep] = useState<ForgotPasswordStep>(1);
  const [email, setEmail] = useState('');
  const [otpCode, setOtpCode] = useState('');

  const emailForm = useForm({ resolver: zodResolver(emailSchema) });
  const otpForm = useForm({ resolver: zodResolver(otpSchema) });
  const passwordForm = useForm({ resolver: zodResolver(passwordSchema) });

  const handleSendOTP = async (data: EmailInput) => {
    try {
      await recoveryService.forgotPassword(data.email);
      setEmail(data.email);
      setStep(2);
    } catch (error: any) {
      emailForm.setError('root', { 
        message: error.response?.data?.message || 'Failed to send verification code' 
      });
    }
  };

  const handleVerifyOTP = async (data: OTPInputData) => {
    try {
      await recoveryService.verifyOtp(email, data.otpCode);
      setOtpCode(data.otpCode);
      setStep(3);
    } catch (error: any) {
      otpForm.setError('root', { 
        message: error.response?.data?.message || 'Invalid verification code' 
      });
    }
  };

  const handleResetPassword = async (data: PasswordInput) => {
    try {
      await recoveryService.resetPassword({ email, otpCode, newPassword: data.newPassword });
      return true;
    } catch (error: any) {
      passwordForm.setError('root', { 
        message: error.response?.data?.message || 'Failed to reset password' 
      });
      return false;
    }
  };

  return { step, setStep, email, emailForm, otpForm, passwordForm, handleSendOTP, handleVerifyOTP, handleResetPassword };
};
