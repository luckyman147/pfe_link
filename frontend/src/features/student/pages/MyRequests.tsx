import React from 'react';
import { DashboardLayout } from '@/components/layout/DashboardLayout';
import { Search, Filter, ExternalLink } from 'lucide-react';
import { RequestCard } from '../components/Requests/RequestCard';

export const MyRequests: React.FC = () => {
  const requests = [
    { id: '1', projectTitle: "AI-Driven Predictive Maintenance", advisor: "Dr. Sarah Chen", date: "Oct 24, 2023", status: "PENDING", description: "Initial proposal submitted for review. Focuses on LSTM models." },
    { id: '2', projectTitle: "Blockchain Supply Chain", advisor: "Prof. Michael Roberts", date: "Oct 20, 2023", status: "REJECTED", description: "Decentralized ledger system for pharma." },
    { id: '3', projectTitle: "Smart Home Energy", advisor: "Dr. Elena Rodriguez", date: "Oct 15, 2023", status: "APPROVED", description: "RL agent for energy optimization." }
  ];

  return (
    <DashboardLayout>
      <div className="max-w-[1200px] mx-auto w-full flex flex-col gap-8 pb-20">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
          <div className="flex flex-col gap-1">
            <h1 className="text-3xl font-bold text-slate-900 tracking-tight">Supervision Requests</h1>
            <p className="text-slate-500 font-medium">Track and manage your project supervision applications.</p>
          </div>
          <div className="flex items-center gap-3">
            <div className="relative group">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
              <input type="text" placeholder="Search requests..." className="pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-xl text-sm focus:ring-primary/20 focus:border-primary transition-all w-64 shadow-sm" />
            </div>
            <button className="flex items-center gap-2 px-4 py-2.5 bg-white border border-slate-200 rounded-xl text-sm font-bold text-slate-600 hover:bg-slate-50 shadow-sm"><Filter className="w-4 h-4" />Filter</button>
          </div>
        </div>

        <div className="grid grid-cols-1 gap-6">
          {requests.map(req => <RequestCard key={req.id} request={req} />)}
        </div>

        <div className="bg-linear-to-r from-primary-500 to-sky-600 rounded-3xl p-8 shadow-xl text-white flex flex-col md:flex-row items-center justify-between gap-8 mt-4 overflow-hidden relative">
          <div className="flex-1 relative z-10">
            <h2 className="text-2xl font-bold mb-2">Need to submit a new proposal?</h2>
            <p className="text-white/80 font-medium">Browse advisors and find the perfect match.</p>
          </div>
          <button className="px-8 py-4 bg-white text-primary font-black rounded-2xl shadow-lg hover:-translate-y-1 transition-all flex items-center gap-2 relative z-10">
            Find New Advisor <ExternalLink className="w-5 h-5" />
          </button>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default MyRequests;
