import api from '@/shared/services/api';
import { ACADEMIC_ENDPOINTS } from '@/config/endpoints';
import type { StudentProfileResponse } from '@/features/pfe/types/project.types';
import type { StudentProfile } from '@/features/academic/types/academic.types';

const studentService = {
  getMyProfile: async (): Promise<StudentProfile> => {
    const { data } = await api.get(ACADEMIC_ENDPOINTS.studentsMe);
    return data.data;
  },

  searchStudents: async (email: string): Promise<StudentProfileResponse[]> => {
    const { data } = await api.get(ACADEMIC_ENDPOINTS.studentsSearch, {
      params: { email },
    });
    return data.data || [];
  },
};

export default studentService;
