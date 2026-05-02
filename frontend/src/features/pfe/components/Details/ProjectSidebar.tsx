import React from 'react';
import { MessageSquare, Clock } from 'lucide-react';

interface ProjectSidebarProps {
  project: {
    advisor: string;
    deadline: string;
    status: string;
  };
}

export const ProjectSidebar: React.FC<ProjectSidebarProps> = ({ project }) => {
  return (
    <div className="flex flex-col gap-6">
      <div className="bg-white border border-slate-200 rounded-3xl p-6 shadow-sm flex flex-col gap-6">
        <h4 className="font-bold text-slate-900 text-sm uppercase tracking-widest">Project Details</h4>
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <span className="text-slate-500 text-sm">Status</span>
            <span className="bg-emerald-50 text-emerald-700 text-xs font-bold px-3 py-1 rounded-full border border-emerald-100">
              {project.status}
            </span>
          </div>
          <div className="flex items-center justify-between">
            <span className="text-slate-500 text-sm">Deadline</span>
            <span className="text-slate-900 text-sm font-bold">{project.deadline}</span>
          </div>
        </div>
        <div className="pt-6 border-t border-slate-100">
          <button className="w-full bg-slate-900 text-white py-3 rounded-xl font-bold hover:bg-slate-800 transition-all flex items-center justify-center gap-2">
            <MessageSquare className="w-4 h-4" />
            Ask a Question
          </button>
        </div>
      </div>

      <div className="bg-linear-to-br from-primary-600 to-sky-700 rounded-3xl p-6 shadow-xl text-white">
        <div className="flex items-center gap-4 mb-6">
          <div className="w-16 h-16 rounded-2xl bg-white/20 backdrop-blur-md flex items-center justify-center text-2xl font-bold">
            {project.advisor.split(' ').map(n => n[0]).join('')}
          </div>
          <div>
            <h4 className="font-bold text-lg">{project.advisor}</h4>
            <p className="text-white/70 text-xs">Professor</p>
          </div>
        </div>
        <button className="w-full bg-white text-primary py-3 rounded-xl font-bold hover:bg-slate-50 transition-all text-sm">
          View Advisor Profile
        </button>
      </div>

      <div className="bg-amber-50 border border-amber-100 rounded-3xl p-6 flex items-start gap-4 text-amber-900">
        <Clock className="w-6 h-6 text-amber-600 shrink-0" />
        <div>
          <h5 className="font-bold text-sm mb-1">Apply Soon</h5>
          <p className="text-amber-700 text-xs leading-relaxed">The application window closes in 5 days.</p>
        </div>
      </div>
    </div>
  );
};
