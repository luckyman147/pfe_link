import React from 'react';
import { useTranslation } from 'react-i18next';
import { useForm } from 'react-hook-form';
import { usePFEs } from '../hooks/usePFEs';

export const PFECreateForm: React.FC<{ onSuccess: () => void }> = ({ onSuccess }) => {
    const { t } = useTranslation();
    const { createPFE, isCreating } = usePFEs();
    const { register, handleSubmit, reset } = useForm();

    const onSubmit = (data: any) => {
        createPFE(data, {
            onSuccess: () => {
                reset();
                onSuccess();
            }
        });
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4 bg-card p-6 border rounded-xl shadow-lg">
            <div>
                <label className="block text-sm font-medium mb-1">{t('pfe.title')}</label>
                <input {...register('title', { required: true })} className="w-full p-2 border rounded-md" />
            </div>
            <div>
                <label className="block text-sm font-medium mb-1">Description</label>
                <textarea {...register('description', { required: true })} className="w-full p-2 border rounded-md h-32" />
            </div>
            <div>
                <label className="block text-sm font-medium mb-1">{t('pfe.places')}</label>
                <input type="number" {...register('capacity', { valueAsNumber: true })} className="w-full p-2 border rounded-md" />
            </div>
            <button 
                type="submit" 
                disabled={isCreating}
                className="w-full bg-primary text-primary-foreground py-2 rounded-lg font-medium hover:opacity-90 disabled:opacity-50 transition"
            >
                {isCreating ? t('pfe.loading') : t('pfe.create')}
            </button>
        </form>
    );
};
