import { useState, useCallback, useEffect } from 'react';
import projectService from '@/features/pfe/services/project.service';
import type {
  ProjectData,
  ProjectInvitation,
  SelectionRequest,
} from '@/features/pfe/types/project.types';
import { useAuth } from '@/features/auth/context/AuthContext';

interface UseMyProjectReturn {
  project: ProjectData | null;
  selection: SelectionRequest | null;
  invitations: ProjectInvitation[];
  isLoading: boolean;
  error: string | null;
  createProject: (title: string, description: string) => Promise<void>;
  inviteFriend: (projectId: string, inviteeId: string) => Promise<void>;
  acceptInvitation: (invitationId: string) => Promise<void>;
  rejectInvitation: (invitationId: string) => Promise<void>;
  submitSelectionRequest: (
    projectId: string,
    advisorProfileId: string,
    message: string
  ) => Promise<void>;
  refetchProject: () => Promise<void>;
  refetchInvitations: () => Promise<void>;
}

export const useMyProject = (): UseMyProjectReturn => {
  const [project, setProject] = useState<ProjectData | null>(null);
  const [selection, setSelection] = useState<SelectionRequest | null>(null);
  const [invitations, setInvitations] = useState<ProjectInvitation[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { user } = useAuth();

  const refetchProject = useCallback(async () => {
    try {
      setIsLoading(true);
      setError(null);
      const projectData = await projectService.getMyProject();
      setProject(projectData);
    } catch (err: any) {
      // 404 is expected when student has no project yet
      if (err.response?.status !== 404) {
        setError(err.response?.data?.message || 'Failed to fetch project');
      }
    } finally {
      setIsLoading(false);
    }
  }, []);

  const refetchInvitations = useCallback(async () => {
    try {
      const invitationsData = await projectService.getMyInvitations();
      setInvitations(invitationsData);
    } catch (err: any) {
      console.error('Failed to fetch invitations:', err);
    }
  }, []);

  const refetchSelection = useCallback(async () => {
    if (!user?.id) return;
    try {
      const selectionData = await projectService.getMySelection(user.id);
      setSelection(selectionData);
    } catch (err: any) {
      console.error('Failed to fetch selection:', err);
    }
  }, [user?.id]);

  const createProject = useCallback(
    async (title: string, description: string) => {
      try {
        setError(null);
        const newProject = await projectService.createProject(title, description);
        setProject(newProject);
      } catch (err: any) {
        setError(err.response?.data?.message || 'Failed to create project');
        throw err;
      }
    },
    []
  );

  const inviteFriend = useCallback(
    async (projectId: string, inviteeId: string) => {
      try {
        setError(null);
        await projectService.inviteFriend(projectId, inviteeId);
        await refetchProject();
      } catch (err: any) {
        setError(err.response?.data?.message || 'Failed to invite friend');
        throw err;
      }
    },
    [refetchProject]
  );

  const acceptInvitation = useCallback(
    async (invitationId: string) => {
      try {
        setError(null);
        await projectService.acceptInvitation(invitationId);
        await refetchProject();
        await refetchInvitations();
      } catch (err: any) {
        setError(err.response?.data?.message || 'Failed to accept invitation');
        throw err;
      }
    },
    [refetchProject, refetchInvitations]
  );

  const rejectInvitation = useCallback(
    async (invitationId: string) => {
      try {
        setError(null);
        await projectService.rejectInvitation(invitationId);
        await refetchInvitations();
      } catch (err: any) {
        setError(err.response?.data?.message || 'Failed to reject invitation');
        throw err;
      }
    },
    [refetchInvitations]
  );

  const submitSelectionRequest = useCallback(
    async (
      projectId: string,
      advisorProfileId: string,
      message: string
    ) => {
      try {
        setError(null);
        const selectionData = await projectService.submitSelectionRequest(
          projectId,
          advisorProfileId,
          message
        );
        setSelection(selectionData);
        await refetchProject();
      } catch (err: any) {
        setError(err.response?.data?.message || 'Failed to submit selection');
        throw err;
      }
    },
    [refetchProject]
  );

  useEffect(() => {
    refetchProject();
    refetchInvitations();
    if (user?.id) {
      refetchSelection();
    }
  }, [user?.id, refetchProject, refetchInvitations, refetchSelection]);

  return {
    project,
    selection,
    invitations,
    isLoading,
    error,
    createProject,
    inviteFriend,
    acceptInvitation,
    rejectInvitation,
    submitSelectionRequest,
    refetchProject,
    refetchInvitations,
  };
};
