import React from 'react';
import { Briefcase, FileText } from 'lucide-react';
import type { AdvisorProfile } from '@/features/academic/types/academic.types';

interface AdvisorCardProps {
  advisor: AdvisorProfile;
  isSelected: boolean;
  onSelect: (advisor: AdvisorProfile) => void;
}

export const AdvisorCard: React.FC<AdvisorCardProps> = ({ advisor, isSelected, onSelect }) => {
  return (
    <div 
      onClick={() => onSelect(advisor)}
      className={`p-6 bg-white rounded-2xl border-2 transition-all cursor-pointer group ${
        isSelected 
        ? 'border-primary-500 shadow-lg shadow-primary-500/10' 
        : 'border-transparent hover:border-gray-200 shadow-sm'
      }`}
    >
      <div className="flex items-start gap-4">
        <div className={`w-14 h-14 rounded-xl flex items-center justify-center text-xl font-bold shadow-sm ${
          isSelected ? 'bg-primary-500 text-white' : 'bg-gray-100 text-gray-500'
        }`}>
          {advisor.fullName.charAt(0)}
        </div>
        <div className="flex-1">
          <h3 className="text-xl font-bold text-navy-900 group-hover:text-primary-600 transition-colors">{advisor.fullName}</h3>
          <div className="flex flex-wrap gap-4 mt-2">
             <span className="inline-flex items-center gap-1.5 text-sm text-gray-500 font-medium">
               <Briefcase className="w-4 h-4" />
               {advisor.department || 'All Departments'}
             </span>
             <span className="inline-flex items-center gap-1.5 text-sm text-gray-500 font-medium">
               <FileText className="w-4 h-4" />
               {advisor.specialization || 'General Supervision'}
             </span>
          </div>
        </div>
        <div className={`w-6 h-6 rounded-full border-2 flex items-center justify-center transition-all ${
          isSelected ? 'border-primary-500 bg-primary-50' : 'border-gray-200'
        }`}>
          {isSelected && <div className="w-3 h-3 bg-primary-500 rounded-full" />}
        </div>
      </div>
    </div>
  );
};
