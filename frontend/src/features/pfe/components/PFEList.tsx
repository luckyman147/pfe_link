import React from 'react';
import { useTranslation } from 'react-i18next';
import { usePFEs } from '../hooks/usePFEs';
import { PFECard } from './PFECard';

export const PFEList: React.FC = () => {
    const { pfes, isLoading } = usePFEs();
    const { t } = useTranslation();

    if (isLoading) {
        return <div className="text-center py-10">{t('pfe.loading')}</div>;
    }

    if (pfes.length === 0) {
        return <div className="text-center py-10 text-muted-foreground">{t('pfe.no_pfes')}</div>;
    }

    return (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {pfes.map((pfe: any) => (
                <PFECard key={pfe.id} pfe={pfe} />
            ))}
        </div>
    );
};
