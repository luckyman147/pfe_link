import { Users } from 'lucide-react';

export const EmptyAdvisorState = () => {
  return (
    <div className="text-center py-20 bg-slate-50 rounded-[40px] border-2 border-dashed border-slate-200">
      <div className="w-20 h-20 bg-white rounded-full flex items-center justify-center mx-auto mb-6 shadow-sm">
        <Users className="w-10 h-10 text-slate-300" />
      </div>
      <h3 className="text-xl font-bold text-slate-900">No Advisors Found</h3>
      <p className="text-slate-500 max-w-sm mx-auto mt-2">
        We couldn't find any advisors matching your faculty at the moment. Please check back later or contact administration.
      </p>
    </div>
  );
};
