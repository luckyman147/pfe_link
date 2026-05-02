import { Loader2 } from 'lucide-react';
import { useAdvisorGallery } from '../hooks/useAdvisorGallery';
import { AdvisorGalleryHeader } from './AdvisorGallery/AdvisorGalleryHeader';
import { AdvisorCard } from './AdvisorGallery/AdvisorCard';
import { EmptyAdvisorState } from './AdvisorGallery/EmptyAdvisorState';

export const AdvisorGallery = () => {
  const { 
    advisors, 
    isLoading, 
    selection, 
    isSelecting, 
    handleSelect 
  } = useAdvisorGallery();

  if (isLoading) {
    return (
      <div className="flex flex-col items-center justify-center h-64 gap-4">
        <Loader2 className="w-10 h-10 text-primary animate-spin" />
        <p className="text-slate-500 font-medium">Finding available advisors...</p>
      </div>
    );
  }

  return (
    <div className="space-y-8 animate-in fade-in slide-in-from-bottom-4 duration-700">
      <AdvisorGalleryHeader selection={selection} />

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {advisors.map((advisor) => (
          <AdvisorCard
            key={advisor.id}
            advisor={advisor}
            selection={selection}
            isSelecting={isSelecting === advisor.id}
            onSelect={handleSelect}
          />
        ))}
      </div>
      
      {advisors.length === 0 && <EmptyAdvisorState />}
    </div>
  );
};
