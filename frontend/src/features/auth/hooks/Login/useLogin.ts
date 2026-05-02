import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useAuth } from '@/features/auth';

const loginSchema = z.object({
  email: z.string().email("Invalid email address"),
  password: z.string().min(1, "Password is required"),
});

export type LoginFormData = z.infer<typeof loginSchema>;

export const useLogin = () => {
  const { login } = useAuth();
  const { 
    register, 
    handleSubmit, 
    formState: { errors, isSubmitting }, 
    setError 
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
  });

  const onSubmit = async (data: LoginFormData) => {
    try {
      await login(data);
      // Navigation is now handled by the Login component's observer of isAuthenticated
    } catch (error: any) {
      console.error(error);
      const errorMessage = error.response?.data?.message || 'Login failed';
      
      if (error.response?.status === 403 || errorMessage.includes('pending')) {
        setError('root', { 
          message: 'Your account is under review. Please wait for admin approval.' 
        });
      } else {
        setError('root', { 
          message: errorMessage
        });
      }
    }
  };

  return {
    register,
    handleSubmit,
    errors,
    isSubmitting,
    onSubmit,
  };
};
