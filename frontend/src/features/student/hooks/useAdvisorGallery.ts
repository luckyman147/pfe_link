import { useEffect, useState, useCallback } from 'react';
import advisorService from '@/features/student/services/advisor.service';
import { useStudentProfile } from './useStudentProfile';
import { useMyProject } from '@/features/pfe/hooks/useMyProject';
import api from '@/shared/services/api';
import type { AdvisorProfile } from '@/features/academic/types/academic.types';

export const useAdvisorGallery = () => {
  const { profile } = useStudentProfile();
  const { project } = useMyProject();
  const [advisors, setAdvisors] = useState<AdvisorProfile[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selection, setSelection] = useState<string | null>(null);
  const [isSelecting, setIsSelecting] = useState(false);

  useEffect(() => {
    const fetchAdvisors = async () => {
      if (!profile?.facultyId) {
        setIsLoading(false);
        return;
      }

      try {
        setError(null);
        const data = await advisorService.getAdvisorsByFaculty(profile.facultyId);
        setAdvisors(data);
      } catch (err: any) {
        setError(err.response?.data?.message || 'Failed to fetch advisors');
        console.error('Failed to fetch advisors:', err);
      } finally {
        setIsLoading(false);
      }
    };

    fetchAdvisors();
  }, [profile?.facultyId]);

  const handleSelect = useCallback(async (advisorId: string, message: string) => {
    if (!project?.id) {
      setError('No project found. Please create a project first.');
      return;
    }

    setIsSelecting(true);
    setError(null);
    try {
      await api.post('/api/v1/selection-requests', {
        projectId: project.id,
        advisorProfileId: advisorId,
        message,
      });
      setSelection(advisorId);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to submit selection request');
    } finally {
      setIsSelecting(false);
    }
  }, [project?.id]);

  return {
    advisors,
    isLoading,
    error,
    selection,
    isSelecting,
    handleSelect,
  };
};