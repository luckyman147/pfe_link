import { useEffect, useState } from 'react';
import { useAuth } from '@/features/auth';
import { advisorService, type AdvisorProfile, type SelectionRequest } from '@/features/auth/services/advisor.service';

export const useAdvisorGallery = () => {
  const { user } = useAuth();
  const [advisors, setAdvisors] = useState<AdvisorProfile[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [selection, setSelection] = useState<SelectionRequest | null>(null);
  const [isSelecting, setIsSelecting] = useState<string | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const facultyName = user?.studentProfile?.facultyName || ""; 
        const advisorsData = await advisorService.searchAdvisors(facultyName);
        setAdvisors(advisorsData);
        
        try {
          const selectionData = await advisorService.getSelectionStatus();
          setSelection(selectionData);
        } catch {
          // No selection yet
        }
      } catch (error) {
        console.error("Failed to fetch advisors", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, [user]);

  const handleSelect = async (advisorId: string) => {
    setIsSelecting(advisorId);
    try {
      const newSelection = await advisorService.selectAdvisor(advisorId);
      setSelection(newSelection);
    } catch (error: unknown) {
      const message = error instanceof Error ? (error as any).response?.data?.message : "Failed to select advisor";
      alert(message || "Failed to select advisor");
    } finally {
      setIsSelecting(null);
    }
  };

  return {
    advisors,
    isLoading,
    selection,
    isSelecting,
    handleSelect
  };
};
