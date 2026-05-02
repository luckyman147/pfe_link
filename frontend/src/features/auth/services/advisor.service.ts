import { api } from "@/services";

export interface Faculty {
  id: string;
  name: string;
}

export interface AdvisorProfile {
  id: string;
  fullName: string;
  specialization: string;
  department: string;
  capacity: number;
  currentStudents: number;
  faculties: Faculty[];
}

export interface SelectionRequest {
  id: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  rejectionReason?: string;
}

export const advisorService = {
  searchAdvisors: async (faculty: string): Promise<AdvisorProfile[]> => {
    const response = await api.get(`/advisors/search?faculty=${faculty}`);
    return response.data;
  },

  selectAdvisor: async (advisorId: string): Promise<SelectionRequest> => {
    const response = await api.post(`/advisors/${advisorId}/select`);
    return response.data;
  },

  getSelectionStatus: async (): Promise<SelectionRequest | null> => {
    // This could be part of the profile or a separate endpoint
    const response = await api.get('/advisors/my-selection');
    return response.data;
  }
};
