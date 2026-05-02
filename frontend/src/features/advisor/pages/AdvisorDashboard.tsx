import React from 'react';
import { DashboardLayout } from '@/components/layout/DashboardLayout';
import { Plus, Calendar } from 'lucide-react';
import { AdvisorStats } from '../components/Dashboard/AdvisorStats';
import { SupervisionList } from '../components/Dashboard/SupervisionList';
import { RequestsSidebar } from '../components/Dashboard/RequestsSidebar';

export const AdvisorDashboard: React.FC = () => {
  return (
    <DashboardLayout>
      <div className="space-y-8 pb-10">
        {/* Page Header - Using Design Tokens */}
        <div className="flex flex-col md:flex-row md:items-end justify-between gap-6">
          <div className="space-y-1">
            <h1 className="text-3xl font-black text-on-surface tracking-tight">Academic Dashboard</h1>
            <p className="text-on-surface-variant font-medium">Managing your student supervision and project lifecycle</p>
          </div>
          <div className="flex gap-3">
            <button className="bg-surface-container-high border border-outline-variant text-on-surface font-bold px-5 py-3 rounded-2xl hover:bg-surface-container-highest transition-all shadow-sm flex items-center gap-2 active:scale-95">
              <Calendar className="w-5 h-5 text-on-surface-variant" />
              Schedule
            </button>
            <button className="bg-primary text-on-primary font-bold px-5 py-3 rounded-2xl hover:bg-primary-container transition-all shadow-lg shadow-primary/20 flex items-center gap-2 active:scale-95">
              <Plus className="w-5 h-5" />
              New Proposal
            </button>
          </div>
        </div>

        {/* Bento Grid for Stats */}
        <AdvisorStats />

        {/* Main Content Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-8">
          {/* Left Column: Current Supervisions */}
          <div className="lg:col-span-8 space-y-6">
            <SupervisionList />
          </div>

          {/* Right Column: Recent Requests */}
          <div className="lg:col-span-4 space-y-6">
            <RequestsSidebar />
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default AdvisorDashboard;
