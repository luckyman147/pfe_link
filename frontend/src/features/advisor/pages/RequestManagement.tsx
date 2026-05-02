import React from 'react';
import { DashboardLayout } from '@/components/layout/DashboardLayout';
import { RequestHeader } from '../components/Requests/RequestHeader';
import { RequestStats } from '../components/Requests/RequestStats';
import { RequestList } from '../components/Requests/RequestList';

export const RequestManagement: React.FC = () => {
  // Mock data for advisor managing requests
  const pendingRequests = [
    {
      id: '1',
      student: "Alex Rivera",
      project: "Cloud-Native Microservices Security Architecture",
      date: "2 hours ago",
      faculty: "Faculty of Engineering",
      avatar: "AR",
      gpa: "3.8/4.0"
    },
    {
      id: '2',
      student: "Jordan Smith",
      project: "Low-Power ML on Edge Devices",
      date: "Yesterday",
      faculty: "IT Institute",
      avatar: "JS",
      gpa: "3.6/4.0"
    },
    {
      id: '3',
      student: "Taylor Wong",
      project: "Post-Quantum Cryptography for Web3",
      date: "Oct 22",
      faculty: "Faculty of Science",
      avatar: "TW",
      gpa: "3.9/4.0"
    }
  ];

  return (
    <DashboardLayout>
      <div className="max-w-[1200px] mx-auto w-full flex flex-col gap-8 pb-20">
        <RequestHeader />
        <RequestStats pendingCount={pendingRequests.length} />
        <RequestList requests={pendingRequests} />
      </div>
    </DashboardLayout>
  );
};

