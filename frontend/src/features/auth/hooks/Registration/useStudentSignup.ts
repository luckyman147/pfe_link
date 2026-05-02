import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useNavigate } from 'react-router-dom';
import { academicService } from '@/features/academic';
import type { Faculty } from '@/features/auth/types/auth.types';
import React from 'react';
import { useAuth } from '@/features/auth';

const schema = z.object({
  fullName: z.string().min(2, 'Full name is required'),
  email: z.string().email('Invalid email'),
  password: z.string().min(8, 'At least 8 characters'),
  telephone: z.string().min(8, 'Telephone is required'),
  cinNumber: z.string().min(8, 'CIN must be 8 digits').max(8),
  facultyId: z.string().min(1, 'Faculty is required'),
  studentCardUrl: z.string().optional(),
  draftId: z.string().optional(),
});

export type StudentFormData = z.infer<typeof schema>;
export { schema };

export const useStudentSignup = () => {
  const navigate = useNavigate();
  const { registerStudent } = useAuth();
  const [faculties, setFaculties] = React.useState<Faculty[]>([]);
  const [cardUrl, setCardUrl] = React.useState('');
  const [draftId, setDraftId] = React.useState<string | undefined>(undefined);

  React.useEffect(() => {
    academicService.getFaculties().then(setFaculties).catch(console.error);
  }, []);

  const form = useForm<StudentFormData>({
    resolver: zodResolver(schema),
  });

  const onSubmit = async (data: StudentFormData) => {
    try {
      await registerStudent({ ...data, studentCardUrl: cardUrl, draftId });
      navigate('/auth/verify-email');
    } catch (error: any) {
      form.setError('root', {
        message: error.response?.data?.message || 'Registration failed',
      });
    }
  };

  const handleCardChange = (url: string, id?: string) => {
    setCardUrl(url);
    setDraftId(id);
    form.setValue('studentCardUrl', url);
    form.setValue('draftId', id);
  };

  return { form, faculties, cardUrl, draftId, handleCardChange, onSubmit };
};
