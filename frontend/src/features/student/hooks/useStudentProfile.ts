import { useState, useEffect, useCallback } from 'react';
import studentService from '@/features/student/services/student.service';
import type { StudentProfile } from '@/features/academic/types/academic.types';

interface UseStudentProfileReturn {
  profile: StudentProfile | null;
  isLoading: boolean;
  error: string | null;
  refetch: () => Promise<void>;
}

export const useStudentProfile = (): UseStudentProfileReturn => {
  const [profile, setProfile] = useState<StudentProfile | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const refetch = useCallback(async () => {
    try {
      setIsLoading(true);
      setError(null);
      const data = await studentService.getMyProfile();
      setProfile(data);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch profile');
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    refetch();
  }, [refetch]);

  return {
    profile,
    isLoading,
    error,
    refetch,
  };
};
