import React from 'react';
import { Users, FileText, Clock, TrendingUp, AlertCircle } from 'lucide-react';

interface AdminMetricsGridProps {
    pendingCount: number;
}

export const AdminMetricsGrid: React.FC<AdminMetricsGridProps> = ({ pendingCount }) => (
    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-white rounded-2xl border border-slate-100 p-6 shadow-sm hover:shadow-xl hover:shadow-slate-200/50 hover:border-primary/20 transition-all duration-300 group">
            <div className="flex items-center justify-between mb-4">
                <span className="text-[11px] font-bold text-slate-400 uppercase tracking-widest">Total Users</span>
                <div className="w-10 h-10 rounded-xl bg-slate-50 text-slate-400 group-hover:bg-primary group-hover:text-white transition-colors duration-300 flex items-center justify-center">
                    <Users className="w-5 h-5" />
                </div>
            </div>
            <div className="space-y-1">
                <h2 className="text-3xl font-black text-slate-900">2,451</h2>
                <div className="flex items-center gap-1.5 text-emerald-600 font-bold text-xs bg-emerald-50 w-fit px-2 py-1 rounded-lg">
                    <TrendingUp className="w-3.5 h-3.5" />
                    12% increase
                </div>
            </div>
        </div>

        <div className="bg-white rounded-2xl border border-slate-100 p-6 shadow-sm hover:shadow-xl hover:shadow-slate-200/50 hover:border-blue-500/20 transition-all duration-300 group">
            <div className="flex items-center justify-between mb-4">
                <span className="text-[11px] font-bold text-slate-400 uppercase tracking-widest">Active PFEs</span>
                <div className="w-10 h-10 rounded-xl bg-slate-50 text-slate-400 group-hover:bg-blue-600 group-hover:text-white transition-colors duration-300 flex items-center justify-center">
                    <FileText className="w-5 h-5" />
                </div>
            </div>
            <div className="space-y-1">
                <h2 className="text-3xl font-black text-slate-900">843</h2>
                <div className="flex items-center gap-1.5 text-emerald-600 font-bold text-xs bg-emerald-50 w-fit px-2 py-1 rounded-lg">
                    <TrendingUp className="w-3.5 h-3.5" />
                    4% increase
                </div>
            </div>
        </div>

        <div className="bg-white rounded-2xl border border-slate-100 p-6 shadow-sm hover:shadow-xl hover:shadow-slate-200/50 hover:border-rose-500/20 transition-all duration-300 group">
            <div className="flex items-center justify-between mb-4">
                <span className="text-[11px] font-bold text-slate-400 uppercase tracking-widest">Pending Approvals</span>
                <div className="w-10 h-10 rounded-xl bg-slate-50 text-slate-400 group-hover:bg-rose-600 group-hover:text-white transition-colors duration-300 flex items-center justify-center">
                    <Clock className="w-5 h-5" />
                </div>
            </div>
            <div className="space-y-1">
                <h2 className="text-3xl font-black text-slate-900">{pendingCount}</h2>
                <div className="flex items-center gap-1.5 text-rose-600 font-bold text-xs bg-rose-50 w-fit px-2 py-1 rounded-lg">
                    <AlertCircle className="w-3.5 h-3.5" />
                    Requires Attention
                </div>
            </div>
        </div>
    </div>
);
