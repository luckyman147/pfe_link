import React from 'react';
import { useTranslation } from 'react-i18next';
import { useAuth, UserRole } from '@/features/auth';
import { PFEList } from '../components/PFEList';
import { PFECreateForm } from '../components/PFECreateForm';

export const PFEDashboard: React.FC = () => {
    const { t } = useTranslation();
    const { user } = useAuth();

    const [showCreate, setShowCreate] = React.useState(false);

    return (
        <div className="space-y-8 animate-in fade-in duration-500">
            <div className="flex justify-between items-center">
                <h2 className="text-3xl font-bold tracking-tight">{t('pfe.dashboard_title')}</h2>
                {user?.role === UserRole.ADVISOR && (
                    <button 
                        onClick={() => setShowCreate(!showCreate)}
                        className="bg-primary text-primary-foreground px-4 py-2 rounded-lg font-medium shadow-md hover:shadow-lg transition-all"
                    >
                        {showCreate ? 'Close' : t('pfe.create')}
                    </button>
                )}
            </div>

            {showCreate && <PFECreateForm onSuccess={() => setShowCreate(false)} />}

            <section>
                <PFEList />
            </section>
        </div>
    );
};

export default PFEDashboard;
