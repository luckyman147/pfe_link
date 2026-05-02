import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Download, PlusCircle } from 'lucide-react';

export const AdminDashboardHeader: React.FC = () => {
    const navigate = useNavigate();
    return (
        <div className="flex flex-col md:flex-row md:items-end justify-between gap-6">
            <div className="space-y-1">
                <h1 className="text-3xl font-bold text-slate-900 tracking-tight">System Overview</h1>
                <p className="text-slate-500 font-medium">High-level metrics and system activity for PFE-Link.</p>
            </div>
            <div className="flex gap-3">
                <button className="bg-white border border-slate-200 text-slate-700 font-semibold px-4 py-2.5 rounded-xl hover:bg-slate-50 transition-all shadow-sm flex items-center gap-2 active:scale-95">
                    <Download className="w-4 h-4 text-slate-400" />
                    Download Report
                </button>
                <button 
                    onClick={() => navigate('/admin/faculties/new')}
                    className="bg-primary text-white font-semibold px-4 py-2.5 rounded-xl hover:bg-primary/90 transition-all shadow-lg shadow-primary/20 flex items-center gap-2 active:scale-95"
                >
                    <PlusCircle className="w-4 h-4" />
                    New Faculty
                </button>
            </div>
        </div>
    );
};
