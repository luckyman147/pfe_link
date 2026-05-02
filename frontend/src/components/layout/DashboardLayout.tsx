import React from 'react';
import { DashboardSidebar } from './DashboardSidebar';
import { DashboardHeader } from './DashboardHeader';

interface DashboardLayoutProps {
  children: React.ReactNode;
}

export const DashboardLayout = ({ children }: DashboardLayoutProps) => {
  return (
    <div className="min-h-screen bg-background flex">
      {/* Sidebar - Desktop Only */}
      <DashboardSidebar />

      {/* Main Content Area Wrapper */}
      <div className="flex-1 flex flex-col lg:ml-[280px] min-w-0 h-screen overflow-hidden">
        {/* Top App Bar */}
        <DashboardHeader />

        {/* Main Content Scrollable Area */}
        <main className="flex-1 overflow-y-auto p-4 md:p-6 lg:p-8 scroll-smooth">
          <div className="w-full animate-in fade-in slide-in-from-bottom-2 duration-500">
            {children}
          </div>
        </main>
      </div>
    </div>
  );
};
