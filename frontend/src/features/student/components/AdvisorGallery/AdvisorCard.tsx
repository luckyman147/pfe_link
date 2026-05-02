import { User, Briefcase, TrendingUp, ChevronRight, Loader2 } from 'lucide-react';
import { type AdvisorProfile, type SelectionRequest } from '@/features/auth/services/advisor.service';

interface AdvisorCardProps {
  advisor: AdvisorProfile;
  selection: SelectionRequest | null;
  isSelecting: boolean;
  onSelect: (id: string) => void;
}

export const AdvisorCard = ({ advisor, selection, isSelecting, onSelect }: AdvisorCardProps) => {
  const availabilityPercentage = (advisor.currentStudents / advisor.capacity) * 100;
  const isFull = advisor.currentStudents >= advisor.capacity;

  return (
    <div className="bg-white rounded-3xl border border-slate-100 p-6 hover:shadow-xl hover:shadow-slate-200/50 hover:border-primary/20 transition-all duration-300 relative overflow-hidden group">
      <div className="absolute top-0 right-0 w-32 h-32 bg-primary/5 rounded-bl-[100px] -mr-16 -mt-16 transition-transform group-hover:scale-110" />
      
      <div className="flex items-start gap-4 mb-6">
        <div className="w-14 h-14 bg-slate-50 border border-slate-100 rounded-2xl flex items-center justify-center text-primary shadow-sm group-hover:bg-primary group-hover:text-white transition-colors duration-300">
          <User className="w-7 h-7" />
        </div>
        <div className="flex-1">
          <h3 className="font-bold text-slate-900 text-lg leading-tight group-hover:text-primary transition-colors">{advisor.fullName}</h3>
          <div className="flex items-center gap-1.5 text-sm text-slate-500 mt-1">
            <Briefcase className="w-4 h-4 text-slate-400" />
            {advisor.specialization}
          </div>
        </div>
      </div>

      <div className="space-y-4 mb-8">
        <div className="flex items-center justify-between">
          <div className="flex flex-col">
            <span className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">Load Capacity</span>
            <span className="text-sm font-bold text-slate-900">{advisor.currentStudents} / {advisor.capacity} Students</span>
          </div>
          <div className="flex items-center gap-1 text-emerald-600 bg-emerald-50 px-2 py-0.5 rounded-lg">
            <TrendingUp className="w-3.5 h-3.5" />
            <span className="text-xs font-bold">{Math.round(100 - availabilityPercentage)}% Available</span>
          </div>
        </div>

        <div className="relative h-2.5 bg-slate-100 rounded-full overflow-hidden shadow-inner">
          <div 
            className={`absolute inset-y-0 left-0 transition-all duration-1000 ease-out ${
              availabilityPercentage > 80 ? 'bg-rose-500' : availabilityPercentage > 50 ? 'bg-amber-500' : 'bg-primary'
            }`}
            style={{ width: `${availabilityPercentage}%` }}
          />
        </div>
      </div>

      <button
        onClick={() => onSelect(advisor.id)}
        disabled={!!selection || isSelecting || isFull}
        className={`w-full py-3.5 rounded-2xl font-bold transition-all flex items-center justify-center gap-2 group/btn relative overflow-hidden ${
          selection?.status === 'PENDING' ? 'bg-amber-100 text-amber-600 cursor-not-allowed border border-amber-200' :
          selection?.status === 'APPROVED' ? 'bg-emerald-100 text-emerald-600 cursor-not-allowed border border-emerald-200' :
          isFull ? 'bg-slate-100 text-slate-400 cursor-not-allowed border border-slate-200' :
          'bg-slate-900 text-white hover:bg-primary shadow-lg shadow-slate-900/10 active:scale-95'
        }`}
      >
        {isSelecting ? (
          <Loader2 className="w-5 h-5 animate-spin" />
        ) : selection ? (
          <>
            {selection.status === 'PENDING' ? 'Waiting for Approval' : 'Mentor Selected'}
          </>
        ) : isFull ? (
          'Capacity Reached'
        ) : (
          <>
            Request Mentorship
            <ChevronRight className="w-4 h-4 transition-transform group-hover/btn:translate-x-1" />
          </>
        )}
      </button>
    </div>
  );
};
