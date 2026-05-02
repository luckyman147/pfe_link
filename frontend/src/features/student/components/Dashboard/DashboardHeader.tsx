import React from 'react';

interface DashboardHeaderProps {
  fullName?: string;
}

export const DashboardHeader: React.FC<DashboardHeaderProps> = ({ fullName }) => (
  <div className="flex flex-col gap-1">
    <h1 className="text-3xl font-bold text-slate-900">
      Welcome back, {fullName || 'Student'}
    </h1>
    <p className="text-slate-500">
      Here is an overview of your final year project status.
    </p>
  </div>
);
