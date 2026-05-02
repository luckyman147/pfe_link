import React from 'react';
import { CheckCircle2, Clock, ArrowRight } from 'lucide-react';

export const ProjectStatusCard: React.FC = () => (
  <div className="md:col-span-8 bg-white border border-slate-200 rounded-2xl shadow-sm p-6 flex flex-col gap-6 relative overflow-hidden group hover:border-primary/30 transition-all duration-300">
    <div className="absolute -right-20 -top-20 w-64 h-64 bg-primary/5 rounded-full blur-3xl group-hover:bg-primary/10 transition-colors duration-500" />
    
    <div className="flex items-center justify-between z-10">
      <h2 className="text-lg font-semibold text-slate-900">Project Status</h2>
      <span className="bg-blue-50 text-blue-700 text-xs font-medium px-3 py-1 rounded-full flex items-center gap-1.5 border border-blue-100">
        <CheckCircle2 className="w-3.5 h-3.5" />
        Submitted
      </span>
    </div>

    <div className="flex-1 flex flex-col justify-center z-10">
      <h3 className="text-2xl font-bold text-slate-900 mb-2">Awaiting Advisor Review</h3>
      <p className="text-slate-500 max-w-lg leading-relaxed">
        Your project proposal "AI-Driven Predictive Maintenance" has been successfully submitted to the department. 
        The advisory board is currently reviewing your documentation.
      </p>
    </div>

    <div className="mt-auto pt-6 border-t border-slate-100 z-10 flex items-center justify-between">
      <div className="flex items-center gap-2 text-slate-400">
        <Clock className="w-4 h-4" />
        <span className="text-xs font-medium">Submitted: Oct 24, 2023</span>
      </div>
      <button className="text-sm font-semibold text-primary hover:underline flex items-center gap-1 group/btn">
        View Submission Details
        <ArrowRight className="w-4 h-4 transition-transform group-hover/btn:translate-x-0.5" />
      </button>
    </div>
  </div>
);
