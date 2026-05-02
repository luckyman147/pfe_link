import React from 'react';
import { Clock, Check, X, MessageSquare, ChevronRight } from 'lucide-react';

interface RequestItemProps {
  request: {
    id: string;
    student: string;
    project: string;
    date: string;
    faculty: string;
    avatar: string;
    gpa: string;
  };
}

export const RequestItem: React.FC<RequestItemProps> = ({ request }) => {
  return (
    <div className="p-6 hover:bg-slate-50/80 transition-colors group cursor-pointer">
      <div className="flex flex-col lg:flex-row lg:items-center gap-6">
        {/* Student Info */}
        <div className="flex items-center gap-4 min-w-[240px]">
          <div className="w-12 h-12 rounded-2xl bg-linear-to-br from-primary-500 to-sky-600 text-white flex items-center justify-center font-bold shadow-lg shadow-primary-500/20">
            {request.avatar}
          </div>
          <div>
            <h4 className="font-bold text-slate-900 group-hover:text-primary transition-colors">{request.student}</h4>
            <p className="text-xs text-slate-500 font-medium">{request.faculty}</p>
          </div>
        </div>

        {/* Project Info */}
        <div className="flex-1">
          <h5 className="text-sm font-bold text-slate-800 mb-1 leading-snug">{request.project}</h5>
          <div className="flex items-center gap-4">
            <span className="text-[11px] font-bold text-emerald-600 bg-emerald-50 px-2 py-0.5 rounded-md border border-emerald-100">GPA: {request.gpa}</span>
            <span className="text-[11px] font-bold text-slate-400 flex items-center gap-1">
              <Clock className="w-3 h-3" />
              {request.date}
            </span>
          </div>
        </div>

        {/* Actions */}
        <div className="flex items-center gap-2">
          <button className="flex items-center gap-2 px-4 py-2.5 bg-emerald-600 text-white rounded-xl text-xs font-bold shadow-lg shadow-emerald-500/20 hover:bg-emerald-700 hover:-translate-y-0.5 transition-all active:scale-[0.98]">
            <Check className="w-4 h-4" />
            Approve
          </button>
          <button className="flex items-center gap-2 px-4 py-2.5 bg-white border border-slate-200 text-slate-600 rounded-xl text-xs font-bold hover:bg-rose-50 hover:text-rose-600 hover:border-rose-200 transition-all active:scale-[0.98]">
            <X className="w-4 h-4" />
            Decline
          </button>
          <button className="p-2.5 bg-white border border-slate-200 text-slate-400 rounded-xl hover:bg-slate-100 hover:text-slate-600 transition-all">
            <MessageSquare className="w-4 h-4" />
          </button>
          <button className="p-2.5 bg-white border border-slate-200 text-slate-400 rounded-xl hover:bg-slate-100 hover:text-slate-600 transition-all">
            <ChevronRight className="w-4 h-4" />
          </button>
        </div>
      </div>
    </div>
  );
};
