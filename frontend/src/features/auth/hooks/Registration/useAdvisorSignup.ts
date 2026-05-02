import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useNavigate } from 'react-router-dom';
import React from 'react';
import { useAuth } from '@/features/auth';

const schema = z.object({
  fullName: z.string().min(2, "Full name is required"),
  email: z.string().email("Invalid email"),
  password: z.string().min(8, "Password must be at least 8 characters"),
  telephone: z.string().min(8, "Phone number is required"),
  cinNumber: z.string().min(8, "CIN Number is required").max(8),
  cinCardUrl: z.string().optional(),
  draftId: z.string().optional(),
});

export type AdvisorFormData = z.infer<typeof schema>;

export const useAdvisorSignup = () => {
  const navigate = useNavigate();
  const { registerAdvisor } = useAuth();
  const [cinUrl, setCinUrl] = React.useState<string>('');
  const [draftId, setDraftId] = React.useState<string | undefined>(undefined);

  const form = useForm<AdvisorFormData>({
    resolver: zodResolver(schema),
  });

  const onSubmit = async (data: AdvisorFormData) => {
    try {
      await registerAdvisor({ ...data, cinCardUrl: cinUrl, draftId });
      navigate('/auth/verify-email');
    } catch (error: any) {
      form.setError('root', { 
        message: error.response?.data?.message || 'Registration failed' 
      });
    }
  };

  const handleCinChange = (url: string, id?: string) => {
    setCinUrl(url);
    setDraftId(id);
    form.setValue('cinCardUrl', url);
    form.setValue('draftId', id);
  };

  return { form, cinUrl, draftId, handleCinChange, onSubmit };
};
