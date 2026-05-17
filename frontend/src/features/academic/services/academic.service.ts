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
import { ACADEMIC_ENDPOINTS } from '@/config/endpoints';

export const academicService = {
  /** Get all validated faculties */
  getFaculties: async (): Promise<Faculty[]> => {
    const response = await api.get<ApiResponse<Faculty[]>>(ACADEMIC_ENDPOINTS.faculties);
    return response.data.data;
  },

  /** Submit a faculty registration request */
  createFaculty: async (data: CreateFacultyRequest): Promise<string> => {
    const response = await api.post<ApiResponse<string>>(ACADEMIC_ENDPOINTS.faculties, data);
    return response.data.data;
  },

  /** Get pending faculties (Admin only) */
  getPendingFaculties: async (): Promise<FacultyResponse[]> => {
    const response = await api.get<ApiResponse<FacultyResponse[]>>(ACADEMIC_ENDPOINTS.pendingFaculties);
    return response.data.data;
  },

  /** Approve faculty (Admin only) */
  approveFaculty: async (id: string): Promise<void> => {
    await api.post(ACADEMIC_ENDPOINTS.approveFaculty(id));
  },

  /** Reject faculty (Admin only) */
  rejectFaculty: async (id: string): Promise<void> => {
    await api.post(ACADEMIC_ENDPOINTS.rejectFaculty(id));
  },

  /** Get advisors by faculty ID */
  getAdvisors: async (facultyId: string): Promise<AdvisorProfile[]> => {
    const response = await api.get<ApiResponse<AdvisorProfile[]>>(ACADEMIC_ENDPOINTS.advisorsByFaculty(facultyId));
    return response.data.data;
  },

  /** Submit a selection request */
  submitSelection: async (data: SubmitSelectionRequest): Promise<void> => {
    await api.post(ACADEMIC_ENDPOINTS.selectionRequests, data);
  },

  /** Get current user's student profile */
  getMyProfile: async (): Promise<StudentProfile> => {
    const response = await api.get<ApiResponse<StudentProfile>>(ACADEMIC_ENDPOINTS.studentsMe);
    return response.data.data;
  }
};
