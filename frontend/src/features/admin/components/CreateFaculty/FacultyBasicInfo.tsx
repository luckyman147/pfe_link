import React from 'react';
import type { UseFormRegister, FieldErrors } from 'react-hook-form';
import { Building2, Mail, Globe, ShieldCheck } from 'lucide-react';
import type { CreateFacultyRequest } from '@/features/academic/types/academic.types';

interface FacultyBasicInfoProps {
    register: UseFormRegister<CreateFacultyRequest>;
    errors: FieldErrors<CreateFacultyRequest>;
}

export const FacultyBasicInfo: React.FC<FacultyBasicInfoProps> = ({ register, errors }) => (
    <div className="bg-white/80 backdrop-blur-xl p-8 rounded-3xl shadow-xl shadow-primary-500/5 border border-white/50 space-y-6">
        <h3 className="text-xl font-bold text-navy-800 flex items-center gap-2 border-b border-gray-100 pb-4">
            <Building2 className="w-5 h-5 text-primary-500" /> Institution Details
        </h3>

        <div className="space-y-4">
            <div>
                <label className="block text-sm font-semibold text-navy-700 mb-1">Faculty Name</label>
                <input
                    {...register('name', { required: 'Name is required' })}
                    className="w-full px-4 py-3 rounded-xl bg-gray-50 border-transparent focus:bg-white focus:border-primary-500 focus:ring-0 transition-all outline-hidden text-sm"
                    placeholder="e.g. Faculty of Sciences"
                />
                {errors.name && <p className="mt-1 text-xs text-red-500">{errors.name.message}</p>}
            </div>

            <div>
                <label className="block text-sm font-semibold text-navy-700 mb-1">Abbreviation</label>
                <input
                    {...register('abbreviation', { required: 'Abbreviation is required' })}
                    className="w-full px-4 py-3 rounded-xl bg-gray-50 border-transparent focus:bg-white focus:border-primary-500 focus:ring-0 transition-all outline-hidden text-sm"
                    placeholder="e.g. FST"
                />
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                    <label className="text-sm font-semibold text-navy-700 mb-1 flex items-center gap-1"><Mail className="w-3 h-3" /> External Email</label>
                    <input
                        {...register('email', { required: 'Email is required' })}
                        className="w-full px-4 py-3 rounded-xl bg-gray-50 border-transparent focus:bg-white focus:border-primary-500 focus:ring-0 transition-all outline-hidden text-sm"
                        placeholder="contact@faculty.tn"
                    />
                </div>
                <div>
                    <label className="text-sm font-semibold text-navy-700 mb-1 flex items-center gap-1"><Globe className="w-3 h-3" /> Website</label>
                    <input
                        {...register('websiteUrl')}
                        className="w-full px-4 py-3 rounded-xl bg-gray-50 border-transparent focus:bg-white focus:border-primary-500 focus:ring-0 transition-all outline-hidden text-sm"
                        placeholder="https://faculty.tn"
                    />
                </div>
            </div>

            <div>
                <label className="text-sm font-semibold text-navy-700 mb-1 flex items-center gap-1">
                    <ShieldCheck className="w-4 h-4 text-primary-500" /> Admin Initial Password
                </label>
                <input
                    {...register('adminPassword', { required: 'Password is required' })}
                    type="password"
                    className="w-full px-4 py-3 rounded-xl bg-gray-50 border-transparent focus:bg-white focus:border-primary-500 focus:ring-0 transition-all outline-hidden text-sm"
                    placeholder="Secure temporary password"
                />
            </div>
        </div>
    </div>
);
