import React from 'react';
import { User, Briefcase, Calendar } from 'lucide-react';

interface ProjectHeaderProps {
  project: {
    title: string;
    advisor: string;
    department: string;
    postedDate: string;
    tags: string[];
  };
}

export const ProjectHeader: React.FC<ProjectHeaderProps> = ({ project }) => {
  return (
    <div className="bg-white border border-slate-200 rounded-3xl p-8 shadow-sm">
      <div className="flex flex-wrap gap-2 mb-6">
        {project.tags.map(tag => (
          <span key={tag} className="px-3 py-1 bg-slate-50 text-slate-600 text-xs font-bold rounded-full border border-slate-100 uppercase tracking-wider">
            {tag}
          </span>
        ))}
      </div>
      
      <h1 className="text-4xl font-extrabold text-slate-900 leading-tight mb-4">
        {project.title}
      </h1>
      
      <div className="flex flex-wrap items-center gap-6 text-slate-500">
        <div className="flex items-center gap-2">
          <div className="w-8 h-8 rounded-full bg-primary/10 flex items-center justify-center text-primary">
            <User className="w-4 h-4" />
          </div>
          <span className="text-sm font-semibold">{project.advisor}</span>
        </div>
        <div className="flex items-center gap-2">
          <Briefcase className="w-4 h-4" />
          <span className="text-sm font-medium">{project.department}</span>
        </div>
        <div className="flex items-center gap-2">
          <Calendar className="w-4 h-4" />
          <span className="text-sm font-medium">Posted {project.postedDate}</span>
        </div>
      </div>
    </div>
  );
};
