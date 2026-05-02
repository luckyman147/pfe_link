import React from 'react';
import { DashboardLayout } from '@/components/layout/DashboardLayout';
import { useAuth } from '@/features/auth';
import { AdvisorGallery } from '../components/AdvisorGallery';
import { 
  DashboardHeader, 
  ProjectStatusCard, 
  QuickActionsCard, 
  ProjectMilestones 
} from '../components/Dashboard';

export const StudentDashboard: React.FC = () => {
  const { user } = useAuth();

  return (
    <DashboardLayout>
      <div className="max-w-[1200px] mx-auto w-full flex flex-col gap-8">
        <DashboardHeader fullName={user?.fullName} />

        <div className="grid grid-cols-1 md:grid-cols-12 gap-6">
          <ProjectStatusCard />
          <QuickActionsCard />
          <ProjectMilestones />
        </div>

        <div className="mt-4">
          <AdvisorGallery />
        </div>
      </div>
    </DashboardLayout>
  );
};

export default StudentDashboard;

