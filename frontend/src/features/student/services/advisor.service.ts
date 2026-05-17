import api from '@/shared/services/api';
import { ACADEMIC_ENDPOINTS } from '@/config/endpoints';
import type { AdvisorProfile } from '@/features/academic/types/academic.types';

const advisorService = {
  getAdvisorsByFaculty: async (
    facultyId: string
  ): Promise<AdvisorProfile[]> => {
    const { data } = await api.get(
      ACADEMIC_ENDPOINTS.advisorsByFaculty(facultyId)
    );
    return data.data || [];
  },
};

export default advisorService;
