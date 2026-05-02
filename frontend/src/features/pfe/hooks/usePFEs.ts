import { useState } from 'react';

export const usePFEs = () => {
    const [pfes, setPfes] = useState<any[]>([]);
    const [isLoading] = useState(false);
    const [isCreating, setIsCreating] = useState(false);

    const createPFE = async (data: any, { onSuccess }: { onSuccess?: () => void } = {}) => {
        setIsCreating(true);
        try {
            // Mocking creation for now
            console.log('Creating PFE:', data);
            setPfes(prev => [...prev, { ...data, id: Math.random().toString() }]);
            onSuccess?.();
        } finally {
            setIsCreating(false);
        }
    };

    return {
        pfes,
        isLoading,
        isCreating,
        createPFE
    };
};
