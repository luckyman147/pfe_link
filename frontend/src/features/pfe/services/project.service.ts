import api from '@/shared/services/api';
import { PROJECT_ENDPOINTS } from '@/config/endpoints';
import type {
  ProjectData,
  ProjectInvitation,
  SelectionRequest,
} from '@/features/pfe/types/project.types';

const projectService = {
  createProject: async (
    title: string,
    description: string
  ): Promise<ProjectData> => {
    const { data } = await api.post(PROJECT_ENDPOINTS.create, {
      title,
      description,
    });
    return data.data;
  },

  getMyProject: async (): Promise<ProjectData> => {
    const { data } = await api.get(PROJECT_ENDPOINTS.myProject);
    return data.data;
  },

  inviteFriend: async (
    projectId: string,
    inviteeId: string
  ): Promise<ProjectInvitation> => {
    const { data } = await api.post(PROJECT_ENDPOINTS.invite, {
      projectId,
      inviteeId,
    });
    return data.data;
  },

  acceptInvitation: async (invitationId: string): Promise<void> => {
    await api.post(PROJECT_ENDPOINTS.acceptInvitation(invitationId));
  },

  rejectInvitation: async (invitationId: string): Promise<void> => {
    await api.post(PROJECT_ENDPOINTS.rejectInvitation(invitationId));
  },

  getMyInvitations: async (): Promise<ProjectInvitation[]> => {
    const { data } = await api.get(PROJECT_ENDPOINTS.invitations);
    return data.data || [];
  },

  submitSelectionRequest: async (
    projectId: string,
    advisorProfileId: string,
    message: string
  ): Promise<SelectionRequest> => {
    const { data } = await api.post(PROJECT_ENDPOINTS.selectionRequests, {
      projectId,
      advisorProfileId,
      message,
    });
    return data.data;
  },

  getMySelection: async (userId: string): Promise<SelectionRequest | null> => {
    const { data } = await api.get(PROJECT_ENDPOINTS.mySelection(userId));
    return data.data || null;
  },
};

export default projectService;
