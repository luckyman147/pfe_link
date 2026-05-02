import React from 'react';
import { PlusCircle, Upload, Info } from 'lucide-react';

export const QuickActionsCard: React.FC = () => (
  <div className="md:col-span-4 bg-white border border-slate-200 rounded-2xl shadow-sm p-6 flex flex-col gap-6 hover:border-primary/30 transition-all duration-300">
    <h2 className="text-lg font-semibold text-slate-900 border-b border-slate-100 pb-3">Quick Actions</h2>
    
    <div className="flex flex-col gap-3">
      <button className="w-full bg-primary text-white rounded-xl py-3 px-4 text-sm font-semibold flex items-center justify-center gap-2 hover:bg-primary/90 transition-all shadow-md shadow-primary/20 active:scale-[0.98]">
        <PlusCircle className="w-5 h-5" />
        Create New Draft
      </button>
      <button className="w-full bg-white border border-slate-200 text-slate-700 rounded-xl py-3 px-4 text-sm font-semibold flex items-center justify-center gap-2 hover:bg-slate-50 transition-all active:scale-[0.98]">
        <Upload className="w-5 h-5" />
        Upload Document
      </button>
    </div>

    <div className="mt-auto bg-slate-50 rounded-xl p-4 border border-slate-100">
      <div className="flex items-start gap-3">
        <Info className="w-5 h-5 text-slate-400 shrink-0 mt-0.5" />
        <p className="text-xs text-slate-500 leading-normal">
          Make sure all supporting documents are in PDF format before uploading.
        </p>
      </div>
    </div>
  </div>
);
