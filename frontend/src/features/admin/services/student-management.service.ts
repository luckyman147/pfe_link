import api from '@/shared/services/api';
import type { StudentProfile } from '@/features/auth/types/auth.types';

/**
 * Admin Student Management Service
 * Handles admin operations for student management
 */
export const adminStudentService = {
  /**
   * Get pending students
   * GET /api/admin/students/pending
   */
  getPendingStudents: async (): Promise<StudentProfile[]> => {
    const response = await api.get<StudentProfile[]>('/api/admin/students/pending');
    return response.data;
  },

  /**
   * Approve student
   * POST /api/admin/students/{id}/approve
   */
  approveStudent: async (id: string): Promise<Record<string, string>> => {
    const response = await api.post<Record<string, string>>(`/api/admin/students/${id}/approve`);
    return response.data;
  },

  /**
   * Reject student
   * POST /api/admin/students/{id}/reject
   */
  rejectStudent: async (id: string, reason: string): Promise<Record<string, string>> => {
    const response = await api.post<Record<string, string>>(`/api/admin/students/${id}/reject`, { reason });
    return response.data;
  }
};
