import React from 'react';
import { DashboardLayout } from '@/components/layout/DashboardLayout';
import { useAdminDashboard } from '../hooks/useAdminDashboard';
import { 
  AdminDashboardHeader, 
  AdminMetricsGrid, 
  PendingApprovals, 
  RecentActivity 
} from '../components/Dashboard';

export const AdminDashboard: React.FC = () => {
    const { 
        pendingStudents, 
        loading, 
        handleApprove, 
        handleReject 
    } = useAdminDashboard();

    return (
        <DashboardLayout>
            <div className="max-w-[1440px] mx-auto space-y-8">
                <AdminDashboardHeader />
                <AdminMetricsGrid pendingCount={pendingStudents.length} />
                
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    <PendingApprovals 
                        pendingStudents={pendingStudents}
                        loading={loading}
                        onApprove={handleApprove}
                        onReject={handleReject}
                    />
                    <RecentActivity />
                </div>
            </div>
        </DashboardLayout>
    );
};

export default AdminDashboard;

