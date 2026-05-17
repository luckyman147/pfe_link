import React from 'react';
import { useNavigate } from 'react-router-dom';
import { DashboardLayout } from '@/components/layout/DashboardLayout';
import { ArrowLeft, Share2, Bookmark } from 'lucide-react';
import { ProjectHeader } from '../components/Details/ProjectHeader';
import { ProjectContent } from '../components/Details/ProjectContent';
import { ProjectSidebar } from '../components/Details/ProjectSidebar';
import { useMyProject } from '../hooks/useMyProject';

export const ProjectDetails: React.FC = () => {
  const navigate = useNavigate();
  const { project, isLoading, error } = useMyProject();

  if (isLoading) {
    return (
      <DashboardLayout>
        <div className="flex items-center justify-center h-64">
          <div className="animate-pulse text-slate-500">Loading project...</div>
        </div>
      </DashboardLayout>
    );
  }

  if (error || !project) {
    return (
      <DashboardLayout>
        <div className="flex flex-col items-center justify-center h-64 gap-4">
          <div className="text-slate-500">No project found.</div>
          <button 
            onClick={() => navigate(-1)} 
            className="text-primary hover:underline"
          >
            Back to Catalog
          </button>
        </div>
      </DashboardLayout>
    );
  }

  return (
    <DashboardLayout>
      <div className="max-w-[1200px] mx-auto w-full flex flex-col gap-8 pb-20">
        <div className="flex items-center justify-between">
          <button onClick={() => navigate(-1)} className="flex items-center gap-2 text-slate-500 hover:text-primary transition-colors font-medium group">
            <ArrowLeft className="w-4 h-4 group-hover:-translate-x-1 transition-transform" />
            Back to Catalog
          </button>
          <div className="flex items-center gap-3">
            <button className="p-2.5 bg-white border border-slate-200 rounded-xl text-slate-500 hover:text-primary transition-all"><Share2 className="w-5 h-5" /></button>
            <button className="p-2.5 bg-white border border-slate-200 rounded-xl text-slate-500 hover:text-primary transition-all"><Bookmark className="w-5 h-5" /></button>
            <button className="bg-primary text-white px-6 py-2.5 rounded-xl font-bold shadow-lg shadow-primary/20 hover:-translate-y-0.5 transition-all">Request Supervision</button>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          <div className="lg:col-span-2 flex flex-col gap-8">
            <ProjectHeader project={project} />
            <ProjectContent project={project} />
          </div>
          <ProjectSidebar project={project} />
        </div>
      </div>
    </DashboardLayout>
  );
};

export default ProjectDetails;