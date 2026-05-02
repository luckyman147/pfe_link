import api from '@/shared/services/api';
import type { ApiResponse } from '@/shared/types/api';
import type {
  CreateFacultyRequest,
  FacultyResponse,
  AdvisorProfile,
  SubmitSelectionRequest,
  StudentProfile
} from '../types/academic.types';
import type { Faculty } from '@/features/auth/types/auth.types';

export const academicService = {
  /**
   * Get all validated faculties
   * GET /api/v1/faculties
   */
  getFaculties: async (): Promise<Faculty[]> => {
    const response = await api.get<ApiResponse<Faculty[]>>('/api/v1/faculties');
    return response.data.data;
  },

  /**
   * Submit a faculty registration request
   * POST /api/v1/faculties
   */
  createFaculty: async (data: CreateFacultyRequest): Promise<string> => {
    const response = await api.post<ApiResponse<string>>('/api/v1/faculties', data);
    return response.data.data;
  },

  /**
   * Get pending faculties (Admin only)
   * GET /api/admin/pending-faculties
   */
  getPendingFaculties: async (): Promise<FacultyResponse[]> => {
    const response = await api.get<ApiResponse<FacultyResponse[]>>('/api/admin/pending-faculties');
    return response.data.data;
  },

  /**
   * Approve faculty (Admin only)
   * POST /api/admin/approve-faculty/{id}
   */
  approveFaculty: async (id: string): Promise<void> => {
    await api.post(`/api/admin/approve-faculty/${id}`);
  },

  /**
   * Reject faculty (Admin only)
   * POST /api/admin/reject-faculty/{id}
   */
  rejectFaculty: async (id: string): Promise<void> => {
    await api.post(`/api/admin/reject-faculty/${id}`);
  },

  /**
   * Get advisors by faculty ID
   * GET /api/v1/advisors/faculty/{facultyId}
   */
  getAdvisors: async (facultyId: string): Promise<AdvisorProfile[]> => {
    const response = await api.get<ApiResponse<AdvisorProfile[]>>(`/api/v1/advisors/faculty/${facultyId}`);
    return response.data.data;
  },

  /**
   * Submit a selection request
   * POST /api/v1/selection-requests
   */
  submitSelection: async (data: SubmitSelectionRequest): Promise<void> => {
    await api.post('/api/v1/selection-requests', data);
  },

  /**
   * Get current user's student profile
   * GET /api/v1/students/me
   */
  getMyProfile: async (): Promise<StudentProfile> => {
    const response = await api.get<ApiResponse<StudentProfile>>('/api/v1/students/me');
    return response.data.data;
  }
};
