import React from 'react';
import { CheckCircle2, Circle } from 'lucide-react';

export const ProjectMilestones: React.FC = () => (
  <div className="md:col-span-12 bg-white border border-slate-200 rounded-2xl shadow-sm p-6 flex flex-col gap-6 hover:border-primary/30 transition-all duration-300">
    <div className="flex items-center justify-between border-b border-slate-100 pb-3">
      <h2 className="text-lg font-semibold text-slate-900">Project Milestones</h2>
      <button className="text-sm font-medium text-primary hover:underline">View Full Timeline</button>
    </div>

    <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
      <div className="bg-slate-50 border border-slate-200 rounded-xl p-4 flex flex-col gap-2 relative overflow-hidden">
        <div className="absolute top-0 left-0 w-1.5 h-full bg-primary" />
        <div className="flex items-center justify-between">
          <span className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">Phase 1</span>
          <CheckCircle2 className="w-5 h-5 text-primary" />
        </div>
        <h4 className="font-bold text-slate-900">Topic Selection</h4>
        <p className="text-xs text-slate-500">Initial research and topic validation with advisor.</p>
      </div>

      <div className="bg-slate-50 border border-slate-200 rounded-xl p-4 flex flex-col gap-2 relative overflow-hidden">
        <div className="absolute top-0 left-0 w-1.5 h-full bg-primary" />
        <div className="flex items-center justify-between">
          <span className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">Phase 2</span>
          <CheckCircle2 className="w-5 h-5 text-primary" />
        </div>
        <h4 className="font-bold text-slate-900">Proposal Draft</h4>
        <p className="text-xs text-slate-500">Writing the formal project proposal and methodology.</p>
      </div>

      <div className="bg-white border-2 border-primary/20 rounded-xl p-4 flex flex-col gap-2 relative shadow-sm ring-1 ring-primary/5">
        <div className="flex items-center justify-between">
          <span className="text-[10px] font-bold text-primary uppercase tracking-widest">Phase 3</span>
          <span className="relative flex h-2.5 w-2.5">
            <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-primary opacity-75" />
            <span className="relative inline-flex rounded-full h-2.5 w-2.5 bg-primary" />
          </span>
        </div>
        <h4 className="font-bold text-slate-900">Proposal Review</h4>
        <p className="text-xs text-slate-500">Advisory board evaluation of the submitted proposal.</p>
      </div>

      <div className="bg-white border border-slate-100 rounded-xl p-4 flex flex-col gap-2 opacity-60">
        <div className="flex items-center justify-between">
          <span className="text-[10px] font-bold text-slate-300 uppercase tracking-widest">Phase 4</span>
          <Circle className="w-5 h-5 text-slate-200" />
        </div>
        <h4 className="font-bold text-slate-400">Implementation</h4>
        <p className="text-xs text-slate-300">Commence core development and data gathering.</p>
      </div>
    </div>
  </div>
);
