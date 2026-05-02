import api from '@/shared/services/api';
import type { 
  UpdateStudentProfileRequest, 
  UpdateAdvisorProfileRequest, 
  ChangePasswordRequest 
} from '../types/auth.types';

/**
 * Profile Service
 * Handles profile management API calls
 */
export const profileService = {
  /**
   * Update student profile
   * PUT /api/profile/student
   */
  updateStudentProfile: async (data: UpdateStudentProfileRequest): Promise<string> => {
    const response = await api.put<string>('/api/profile/student', data);
    return response.data;
  },

  /**
   * Update advisor profile
   * PUT /api/profile/advisor
   */
  updateAdvisorProfile: async (data: UpdateAdvisorProfileRequest): Promise<string> => {
    const response = await api.put<string>('/api/profile/advisor', data);
    return response.data;
  },

  /**
   * Change password
   * POST /api/profile/change-password
   */
  changePassword: async (data: ChangePasswordRequest): Promise<string> => {
    const response = await api.post<string>('/api/profile/change-password', data);
    return response.data;
  }
};
