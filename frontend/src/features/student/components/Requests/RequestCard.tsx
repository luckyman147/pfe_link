import React from 'react';
import { Clock, CheckCircle2, XCircle, MessageSquare, MoreVertical, ArrowUpRight } from 'lucide-react';

interface RequestCardProps {
  request: {
    id: string;
    projectTitle: string;
    advisor: string;
    date: string;
    status: string;
    description: string;
  };
}

export const RequestCard: React.FC<RequestCardProps> = ({ request }) => {
  const getStatusStyle = (status: string) => {
    switch (status) {
      case 'PENDING': return 'bg-amber-50 text-amber-700 border-amber-100';
      case 'APPROVED': return 'bg-emerald-50 text-emerald-700 border-emerald-100';
      case 'REJECTED': return 'bg-rose-50 text-rose-700 border-rose-100';
      default: return 'bg-slate-50 text-slate-700 border-slate-100';
    }
  };

  const getStatusIcon = (status: string) => {
    switch (status) {
      case 'PENDING': return <Clock className="w-3.5 h-3.5" />;
      case 'APPROVED': return <CheckCircle2 className="w-3.5 h-3.5" />;
      case 'REJECTED': return <XCircle className="w-3.5 h-3.5" />;
      default: return null;
    }
  };

  return (
    <div className="bg-white border border-slate-200 rounded-2xl p-6 shadow-sm hover:border-primary/30 hover:shadow-md transition-all group">
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
        <div className="flex-1 flex flex-col gap-4">
          <div className="flex items-center gap-3">
            <div className={`px-3 py-1 rounded-full text-[10px] font-black uppercase tracking-widest border flex items-center gap-1.5 ${getStatusStyle(request.status)}`}>
              {getStatusIcon(request.status)}
              {request.status}
            </div>
            <span className="text-xs font-bold text-slate-400 uppercase tracking-widest flex items-center gap-1.5">
              <Clock className="w-3.5 h-3.5" />
              Submitted {request.date}
            </span>
          </div>
          <div>
            <h3 className="text-xl font-bold text-slate-900 mb-2 group-hover:text-primary transition-colors">{request.projectTitle}</h3>
            <p className="text-slate-500 text-sm leading-relaxed max-w-2xl line-clamp-2">{request.description}</p>
          </div>
          <div className="flex items-center gap-2">
            <div className="w-7 h-7 rounded-full bg-slate-100 flex items-center justify-center text-xs font-bold text-slate-500">{request.advisor.charAt(0)}</div>
            <span className="text-sm font-semibold text-slate-700">{request.advisor}</span>
          </div>
        </div>
        <div className="flex items-center gap-2 md:self-end lg:self-center">
          <button className="flex items-center gap-2 px-4 py-2 bg-slate-50 text-slate-600 rounded-xl text-xs font-bold hover:bg-primary hover:text-white transition-all">
            <MessageSquare className="w-3.5 h-3.5" />
            Chat
          </button>
          <button className="p-2.5 bg-slate-50 text-slate-400 rounded-xl hover:bg-slate-100 transition-all"><MoreVertical className="w-4 h-4" /></button>
          <button className="p-2.5 bg-primary/10 text-primary rounded-xl hover:bg-primary transition-all"><ArrowUpRight className="w-4 h-4" /></button>
        </div>
      </div>
    </div>
  );
};
