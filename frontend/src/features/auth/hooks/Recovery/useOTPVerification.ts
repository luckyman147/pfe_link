import { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { registrationService } from '@/features/auth/services';

const otpSchema = z.object({
  otp: z.string().length(6, 'OTP must be 6 digits'),
});

type OTPFormData = z.infer<typeof otpSchema>;

export const useOTPVerification = () => {
  const [timer, setTimer] = useState(59);
  
  const {
    handleSubmit,
    setValue,
    watch,
    formState: { errors },
  } = useForm<OTPFormData>({
    resolver: zodResolver(otpSchema),
    defaultValues: { otp: '' },
  });

  useEffect(() => {
    const interval = setInterval(() => {
      setTimer((prev) => (prev > 0 ? prev - 1 : 0));
    }, 1000);
    return () => clearInterval(interval);
  }, []);

  const formatTime = (seconds: number) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  };

  const onVerify = async (data: OTPFormData, email: string) => {
    try {
      console.log('Verifying OTP for:', email, 'code:', data.otp);
      await registrationService.verifyOtp(email, data.otp);
      // Success logic - maybe redirect to login or dashboard
      return true;
    } catch (error) {
      console.error('OTP verification failed:', error);
      return false;
    }
  };

  const resendCode = async (email: string) => {
    try {
      await registrationService.resendVerification(email);
      setTimer(59);
    } catch (error) {
      console.error('Failed to resend code:', error);
    }
  };

  return { handleSubmit, setValue, watch, errors, timer, formatTime, onVerify, resendCode };
};
