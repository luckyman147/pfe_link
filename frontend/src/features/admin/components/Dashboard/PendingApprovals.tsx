import React from 'react';
import { UserPlus, CheckCircle2, GraduationCap, XCircle, ArrowRight } from 'lucide-react';

interface PendingApprovalsProps {
    pendingStudents: any[];
    loading: boolean;
    onApprove: (id: string) => void;
    onReject: (id: string) => void;
}

export const PendingApprovals: React.FC<PendingApprovalsProps> = ({ 
    pendingStudents, 
    loading, 
    onApprove, 
    onReject 
}) => (
    <div className="lg:col-span-2 bg-white border border-slate-100 rounded-3xl shadow-sm overflow-hidden border-slate-200/60 flex flex-col">
        <div className="px-6 py-5 border-b border-slate-100 flex justify-between items-center bg-slate-50/50">
            <h3 className="font-bold text-slate-900 flex items-center gap-2">
                <UserPlus className="w-5 h-5 text-primary" />
                Pending Student Approvals
            </h3>
            <span className="bg-primary/10 text-primary font-bold text-[10px] px-2.5 py-1 rounded-lg uppercase tracking-wider border border-primary/20">
                {pendingStudents.length} Applications
            </span>
        </div>
        <div className="divide-y divide-slate-100 overflow-y-auto max-h-[600px]">
            {loading ? (
                <div className="p-12 text-center">
                    <div className="w-10 h-10 border-4 border-primary/20 border-t-primary rounded-full animate-spin mx-auto mb-4" />
                    <p className="text-slate-400 font-medium italic">Synchronizing applications...</p>
                </div>
            ) : pendingStudents.length === 0 ? (
                <div className="p-12 text-center space-y-3">
                    <div className="w-16 h-16 bg-slate-50 rounded-full flex items-center justify-center mx-auto text-slate-300">
                        <CheckCircle2 className="w-8 h-8" />
                    </div>
                    <h4 className="font-bold text-slate-900 text-lg">All caught up!</h4>
                    <p className="text-slate-500 text-sm max-w-xs mx-auto">There are no pending student applications requiring your review at this time.</p>
                </div>
            ) : (
                pendingStudents.map((profile) => (
                    <div key={profile.id} className="p-6 hover:bg-slate-50/80 transition-all flex items-center justify-between group">
                        <div className="flex items-center gap-4">
                            <div className="w-12 h-12 bg-slate-100 rounded-2xl flex items-center justify-center text-slate-400 group-hover:bg-primary/10 group-hover:text-primary transition-colors">
                                <GraduationCap className="w-6 h-6" />
                            </div>
                            <div className="space-y-0.5">
                                <h4 className="font-bold text-slate-900 group-hover:text-primary transition-colors">{profile.fullName}</h4>
                                <div className="flex items-center gap-2 text-xs font-bold text-slate-400 uppercase tracking-widest">
                                    <span>{profile.user.email}</span>
                                    <span className="w-1 h-1 bg-slate-300 rounded-full" />
                                    <span>{profile.facultyName}</span>
                                </div>
                            </div>
                        </div>
                        <div className="flex items-center gap-2">
                            <button 
                                onClick={() => onApprove(profile.id)}
                                className="p-2.5 bg-emerald-50 text-emerald-600 rounded-xl hover:bg-emerald-600 hover:text-white transition-all shadow-sm active:scale-90"
                                title="Approve Application"
                            >
                                <CheckCircle2 className="w-5 h-5" />
                            </button>
                            <button 
                                onClick={() => onReject(profile.id)}
                                className="p-2.5 bg-rose-50 text-rose-600 rounded-xl hover:bg-rose-600 hover:text-white transition-all shadow-sm active:scale-90"
                                title="Reject Application"
                            >
                                <XCircle className="w-5 h-5" />
                            </button>
                        </div>
                    </div>
                ))
            )}
        </div>
        <div className="p-5 border-t border-slate-100 bg-slate-50/30 text-center">
            <button className="text-slate-400 hover:text-slate-900 font-bold text-xs uppercase tracking-widest transition-colors flex items-center gap-1.5 mx-auto">
                View Full History
                <ArrowRight className="w-3.5 h-3.5" />
            </button>
        </div>
    </div>
);
