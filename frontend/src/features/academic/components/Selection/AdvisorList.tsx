import React from 'react';
import { AdvisorCard } from './AdvisorCard';
import type { AdvisorProfile } from '@/features/academic/types/academic.types';

interface AdvisorListProps {
  advisors: AdvisorProfile[];
  selectedAdvisorId?: string;
  onSelect: (advisor: AdvisorProfile) => void;
}

export const AdvisorList: React.FC<AdvisorListProps> = ({ advisors, selectedAdvisorId, onSelect }) => {
  if (advisors.length === 0) {
    return (
      <div className="p-12 text-center bg-gray-50 rounded-3xl border-2 border-dashed border-gray-200 lg:col-span-2">
        <p className="text-gray-400 font-medium">No advisors found matching your search</p>
      </div>
    );
  }

  return (
    <div className="lg:col-span-2 space-y-4 max-h-[70vh] overflow-y-auto pr-2 custom-scrollbar">
      {advisors.map(advisor => (
        <AdvisorCard 
          key={advisor.id} 
          advisor={advisor} 
          isSelected={selectedAdvisorId === advisor.id}
          onSelect={onSelect}
        />
      ))}
    </div>
  );
};
