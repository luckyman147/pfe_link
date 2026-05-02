import React from 'react';
import { FileText, CheckCircle2, Tag } from 'lucide-react';

interface ProjectContentProps {
  project: {
    description: string;
    requirements: string[];
    deliverables: string[];
  };
}

export const ProjectContent: React.FC<ProjectContentProps> = ({ project }) => {
  return (
    <div className="bg-white border border-slate-200 rounded-3xl p-8 shadow-sm">
      <div className="prose prose-slate max-w-none">
        <h3 className="text-xl font-bold text-slate-900 flex items-center gap-2 mb-4">
          <FileText className="w-5 h-5 text-primary" />
          Project Description
        </h3>
        <p className="text-slate-600 leading-relaxed text-lg mb-8">
          {project.description}
        </p>

        <h3 className="text-xl font-bold text-slate-900 flex items-center gap-2 mb-4">
          <CheckCircle2 className="w-5 h-5 text-primary" />
          Technical Requirements
        </h3>
        <ul className="grid grid-cols-1 md:grid-cols-2 gap-3 mb-8">
          {project.requirements.map((req, i) => (
            <li key={i} className="flex items-start gap-3 bg-slate-50 p-4 rounded-2xl border border-slate-100">
              <div className="w-2 h-2 rounded-full bg-primary mt-2 shrink-0" />
              <span className="text-slate-700 font-medium text-sm">{req}</span>
            </li>
          ))}
        </ul>

        <h3 className="text-xl font-bold text-slate-900 flex items-center gap-2 mb-4">
          <Tag className="w-5 h-5 text-primary" />
          Expected Deliverables
        </h3>
        <div className="flex flex-col gap-3">
          {project.deliverables.map((del, i) => (
            <div key={i} className="flex items-center gap-4 p-4 rounded-2xl border border-slate-100 hover:border-primary/20 transition-colors">
              <div className="w-10 h-10 rounded-xl bg-white shadow-sm flex items-center justify-center font-bold text-primary border border-slate-100">
                0{i+1}
              </div>
              <span className="text-slate-700 font-semibold">{del}</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
