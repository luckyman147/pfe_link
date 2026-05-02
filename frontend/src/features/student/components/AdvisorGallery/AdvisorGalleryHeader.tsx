import { GraduationCap, Clock, CheckCircle2 } from 'lucide-react';
import { type SelectionRequest } from '@/features/auth/services/advisor.service';

interface AdvisorGalleryHeaderProps {
  selection: SelectionRequest | null;
}

export const AdvisorGalleryHeader = ({ selection }: AdvisorGalleryHeaderProps) => {
  return (
    <div className="flex flex-col md:flex-row md:items-end justify-between gap-4">
      <div className="space-y-1">
        <div className="flex items-center gap-2 text-primary font-semibold text-sm uppercase tracking-wider">
          <GraduationCap className="w-4 h-4" />
          Academic Mentorship
        </div>
        <h2 className="text-2xl font-bold text-slate-900">Available Advisors</h2>
        <p className="text-slate-500">Find and select an expert mentor for your PFE journey</p>
      </div>
      
      {selection && (
        <div className={`flex items-center gap-3 px-5 py-2.5 rounded-2xl border shadow-sm transition-all ${
          selection.status === 'PENDING' ? 'bg-amber-50 border-amber-200 text-amber-700' :
          selection.status === 'APPROVED' ? 'bg-emerald-50 border-emerald-200 text-emerald-700' :
          'bg-rose-50 border-rose-200 text-rose-700'
        }`}>
          {selection.status === 'PENDING' && <Clock className="w-5 h-5 animate-pulse" />}
          {selection.status === 'APPROVED' && <CheckCircle2 className="w-5 h-5" />}
          <div className="flex flex-col">
            <span className="text-[10px] uppercase font-bold tracking-tight opacity-70">Current Selection</span>
            <span className="font-bold text-sm leading-tight">
              {selection.status === 'PENDING' ? 'Pending Approval' : selection.status}
            </span>
          </div>
        </div>
      )}
    </div>
  );
};
