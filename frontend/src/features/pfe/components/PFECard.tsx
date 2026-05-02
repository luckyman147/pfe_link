import React from 'react';
import { useTranslation } from 'react-i18next';

interface PFEProps {
    pfe: {
        id: string;
        title: string;
        description: string;
        status: string;
        capacity: number;
        teacher: { email: string };
    };
}

export const PFECard: React.FC<PFEProps> = ({ pfe }) => {
    const { t } = useTranslation();

    return (
        <div className="bg-card border rounded-xl p-5 shadow-sm hover:shadow-md transition-shadow">
            <h3 className="text-xl font-semibold text-primary">{pfe.title}</h3>
            <p className="text-muted-foreground mt-2 line-clamp-3">{pfe.description}</p>
            <div className="mt-4 flex flex-wrap gap-2 items-center justify-between">
                <span className="text-sm font-medium px-2 py-1 bg-accent rounded text-accent-foreground">
                    {pfe.capacity} {t('pfe.places')}
                </span>
                <span className="text-xs text-muted-foreground">
                    {pfe.teacher.email}
                </span>
            </div>
            <button className="w-full mt-4 bg-primary text-primary-foreground py-2 rounded-lg font-medium hover:opacity-90 transition">
                {t('pfe.apply')}
            </button>
        </div>
    );
};
