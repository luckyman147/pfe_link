import React from 'react';
import type { UseFormRegister, UseFormWatch } from 'react-hook-form';
import { MapPin } from 'lucide-react';
import type { CreateFacultyRequest } from '@/features/academic/types/academic.types';
import AddressPicker from '@/shared/components/AddressPicker';

interface FacultyLocationInfoProps {
    register: UseFormRegister<CreateFacultyRequest>;
    watch: UseFormWatch<CreateFacultyRequest>;
    onAddressSelect: (address: any) => void;
}

export const FacultyLocationInfo: React.FC<FacultyLocationInfoProps> = ({ 
    register, 
    watch, 
    onAddressSelect 
}) => {
    const displayName = watch('displayName');
    const path = watch('path');

    return (
        <div className="bg-white/80 backdrop-blur-xl p-8 rounded-3xl shadow-xl shadow-primary-500/5 border border-white/50 space-y-6">
            <h3 className="text-xl font-bold text-navy-800 flex items-center gap-2 border-b border-gray-100 pb-4">
                <MapPin className="w-5 h-5 text-primary-500" /> Geographic Location
            </h3>

            <AddressPicker onAddressSelect={onAddressSelect} />

            {displayName && (
                <div className="p-4 bg-primary-50/50 rounded-2xl border border-primary-100 animate-fade-in text-sm">
                    <p className="text-xs font-bold text-primary-700 uppercase tracking-wider mb-1">Selected Address:</p>
                    <p className="text-navy-800 font-medium">{displayName}</p>
                    <div className="mt-2 text-[10px] text-gray-400 overflow-hidden text-ellipsis">
                        Path: <span className="font-mono">{path}</span>
                    </div>
                </div>
            )}

            <input type="hidden" {...register('path')} />
            <input type="hidden" {...register('displayName')} />
        </div>
    );
};
