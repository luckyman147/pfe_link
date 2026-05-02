import React from 'react';
import { UserPlus, FileText, AlertCircle, CheckCircle2 } from 'lucide-react';

const ACTIVITIES = [
    { icon: UserPlus, color: 'text-blue-600', bg: 'bg-blue-50', text: 'Sarah Jenkins created a new account', time: '2 mins ago' },
    { icon: FileText, color: 'text-emerald-600', bg: 'bg-emerald-50', text: 'Team Alpha submitted final report', time: '1 hr ago' },
    { icon: AlertCircle, color: 'text-rose-600', bg: 'bg-rose-50', text: 'Database backup failed', time: '3 hrs ago' },
    { icon: CheckCircle2, color: 'text-amber-600', bg: 'bg-amber-50', text: 'Dr. Smith approved "AI Vision"', time: '5 hrs ago' }
];

export const RecentActivity: React.FC = () => (
    <div className="bg-white border border-slate-100 rounded-3xl shadow-sm overflow-hidden flex flex-col h-full border-slate-200/60">
        <div className="px-6 py-5 border-b border-slate-100 bg-slate-50/50">
            <h3 className="font-bold text-slate-900">Recent Activity</h3>
        </div>
        <div className="flex-1 overflow-y-auto p-2 space-y-1">
            {ACTIVITIES.map((activity, i) => (
                <div key={i} className="p-4 hover:bg-slate-50 rounded-2xl transition-all flex gap-4 group/item">
                    <div className={`w-10 h-10 rounded-xl ${activity.bg} ${activity.color} flex items-center justify-center shrink-0 shadow-sm transition-transform group-hover/item:scale-110`}>
                        <activity.icon className="w-5 h-5" />
                    </div>
                    <div className="space-y-0.5">
                        <p className="text-sm font-semibold text-slate-800 leading-snug">{activity.text}</p>
                        <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">{activity.time}</p>
                    </div>
                </div>
            ))}
        </div>
        <div className="p-5 border-t border-slate-100 bg-slate-50/30 text-center">
            <button className="text-primary hover:text-primary/80 font-bold text-xs uppercase tracking-widest transition-colors">
                System Logs
            </button>
        </div>
    </div>
);
