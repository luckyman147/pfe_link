import React from 'react';
import { Inbox, ShieldCheck, AlertCircle } from 'lucide-react';

interface RequestStatsProps {
  pendingCount: number;
}

export const RequestStats: React.FC<RequestStatsProps> = ({ pendingCount }) => {
  return (
    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div className="bg-white border border-slate-200 p-6 rounded-2xl shadow-sm flex items-center gap-4">
        <div className="w-12 h-12 rounded-xl bg-blue-50 text-blue-600 flex items-center justify-center">
          <Inbox className="w-6 h-6" />
        </div>
        <div>
          <p className="text-xs font-bold text-slate-400 uppercase tracking-widest">New Requests</p>
          <h3 className="text-2xl font-black text-slate-900">{pendingCount}</h3>
        </div>
      </div>
      <div className="bg-white border border-slate-200 p-6 rounded-2xl shadow-sm flex items-center gap-4">
        <div className="w-12 h-12 rounded-xl bg-emerald-50 text-emerald-600 flex items-center justify-center">
          <ShieldCheck className="w-6 h-6" />
        </div>
        <div>
          <p className="text-xs font-bold text-slate-400 uppercase tracking-widest">Supervising</p>
          <h3 className="text-2xl font-black text-slate-900">8 / 10</h3>
        </div>
      </div>
      <div className="bg-white border border-slate-200 p-6 rounded-2xl shadow-sm flex items-center gap-4">
        <div className="w-12 h-12 rounded-xl bg-amber-50 text-amber-600 flex items-center justify-center">
          <AlertCircle className="w-6 h-6" />
        </div>
        <div>
          <p className="text-xs font-bold text-slate-400 uppercase tracking-widest">Deadlines Soon</p>
          <h3 className="text-2xl font-black text-slate-900">3</h3>
        </div>
      </div>
    </div>
  );
};
