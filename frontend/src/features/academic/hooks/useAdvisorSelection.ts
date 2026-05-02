import { useEffect, useState } from 'react';
import { useAuth } from '@/features/auth';
import { academicService } from '@/features/academic/services/academic.service';
import type { AdvisorProfile, StudentProfile } from '@/features/academic/types/academic.types';

export const useAdvisorSelection = () => {
  const { user } = useAuth();
  const [profile, setProfile] = useState<StudentProfile | null>(null);
  const [advisors, setAdvisors] = useState<AdvisorProfile[]>([]);
  const [selectedAdvisor, setSelectedAdvisor] = useState<AdvisorProfile | null>(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const studentProfile = await academicService.getMyProfile();
        setProfile(studentProfile);
        
        if (studentProfile.status === 'APPROVED') {
          const advisorList = await academicService.getAdvisors(studentProfile.facultyId);
          setAdvisors(advisorList);
        }
      } catch (error) {
        console.error("Failed to fetch data:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const filteredAdvisors = advisors.filter(advisor => 
    advisor.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    advisor.department?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    advisor.specialization?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const submitSelection = async (data: { projectTitle: string; message: string }) => {
    if (!profile || !selectedAdvisor) return;

    setSubmitting(true);
    try {
      await academicService.submitSelection({
        studentUserId: user?.id || '',
        advisorProfileId: selectedAdvisor.id,
        facultyId: profile.facultyId,
        projectTitle: data.projectTitle,
        message: data.message
      });
      setSelectedAdvisor(null);
      return true;
    } catch (error) {
      console.error(error);
      throw error;
    } finally {
      setSubmitting(false);
    }
  };

  return {
    profile,
    selectedAdvisor,
    setSelectedAdvisor,
    loading,
    submitting,
    searchTerm,
    setSearchTerm,
    filteredAdvisors,
    submitSelection
  };
};
