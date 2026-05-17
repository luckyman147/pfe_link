import { useState } from 'react';
import { useMyProject } from './useMyProject';

export function usePFEs() {
    const { project, isLoading, error, createProject } = useMyProject();
    const [isCreating, setIsCreating] = useState(false);
    const [createError, setCreateError] = useState<string | null>(null);

    const createPFE = async (title: string, description: string) => {
        setIsCreating(true);
        setCreateError(null);
        try {
            await createProject(title, description);
        } catch (err: any) {
            setCreateError(err?.response?.data?.message ?? 'Failed to create project');
        } finally {
            setIsCreating(false);
        }
    };

    return {
        project,
        isLoading,
        error,
        isCreating,
        createError,
        createPFE
    };
}