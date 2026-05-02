import React from 'react';
import { FolderOpen, Mail, Users } from 'lucide-react';

export const AdvisorStats: React.FC = () => {
  return (
    <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
      <div className="bg-surface-container-lowest rounded-3xl border border-outline-variant/30 p-8 shadow-sm hover:shadow-2xl hover:shadow-primary/5 hover:border-primary/30 transition-all duration-500 group relative overflow-hidden">
        <div className="absolute top-0 right-0 w-32 h-32 bg-primary/5 rounded-bl-[80px] -mr-8 -mt-8 transition-transform duration-700 group-hover:scale-110 group-hover:bg-primary/10" />
        <div className="flex justify-between items-start relative z-10">
          <div className="w-14 h-14 rounded-2xl bg-primary/10 flex items-center justify-center text-primary group-hover:bg-primary group-hover:text-on-primary transition-all duration-500 shadow-inner">
            <FolderOpen className="w-7 h-7" />
          </div>
          <div className="flex flex-col items-end gap-1">
            <span className="bg-primary/10 text-primary font-bold text-[10px] uppercase tracking-widest px-3 py-1.5 rounded-full border border-primary/10">
              +2 this week
            </span>
          </div>
        </div>
        <div className="mt-8 relative z-10">
          <p className="text-[11px] font-black text-on-surface-variant/50 uppercase tracking-[0.2em]">Active Projects</p>
          <h2 className="text-5xl font-black text-on-surface mt-2 tracking-tighter">12</h2>
        </div>
      </div>

      <div className="bg-surface-container-lowest rounded-3xl border border-outline-variant/30 p-8 shadow-sm hover:shadow-2xl hover:shadow-tertiary/5 hover:border-tertiary/30 transition-all duration-500 group relative overflow-hidden">
        <div className="absolute top-0 right-0 w-32 h-32 bg-tertiary/5 rounded-bl-[80px] -mr-8 -mt-8 transition-transform duration-700 group-hover:scale-110 group-hover:bg-tertiary/10" />
        <div className="flex justify-between items-start relative z-10">
          <div className="w-14 h-14 rounded-2xl bg-tertiary/10 flex items-center justify-center text-tertiary group-hover:bg-tertiary group-hover:text-on-tertiary transition-all duration-500 shadow-inner">
            <Mail className="w-7 h-7" />
          </div>
          <span className="bg-error/10 text-error font-bold text-[10px] uppercase tracking-widest px-3 py-1.5 rounded-full border border-error/10 animate-pulse">
            Action Required
          </span>
        </div>
        <div className="mt-8 relative z-10">
          <p className="text-[11px] font-black text-on-surface-variant/50 uppercase tracking-[0.2em]">Pending Requests</p>
          <h2 className="text-5xl font-black text-on-surface mt-2 tracking-tighter">5</h2>
        </div>
      </div>

      <div className="bg-surface-container-lowest rounded-3xl border border-outline-variant/30 p-8 shadow-sm hover:shadow-2xl hover:shadow-on-surface/5 hover:border-on-surface/20 transition-all duration-500 group relative overflow-hidden">
        <div className="absolute top-0 right-0 w-32 h-32 bg-on-surface/5 rounded-bl-[80px] -mr-8 -mt-8 transition-transform duration-700 group-hover:scale-110 group-hover:bg-on-surface/10" />
        <div className="flex justify-between items-start relative z-10">
          <div className="w-14 h-14 rounded-2xl bg-surface-container-high flex items-center justify-center text-on-surface group-hover:bg-on-surface group-hover:text-on-primary transition-all duration-500 shadow-inner">
            <Users className="w-7 h-7" />
          </div>
          <span className="text-on-surface-variant/60 font-bold text-[10px] uppercase tracking-widest px-3 py-1.5 rounded-full border border-outline-variant/30">
            Cap: 20
          </span>
        </div>
        <div className="mt-8 relative z-10">
          <p className="text-[11px] font-black text-on-surface-variant/50 uppercase tracking-[0.2em]">Students Supervised</p>
          <div className="flex items-end gap-2 mt-2">
            <h2 className="text-5xl font-black text-on-surface tracking-tighter">18</h2>
            <span className="text-xl font-bold text-on-surface-variant/40 mb-1.5">/ 20</span>
          </div>
        </div>
      </div>
    </div>
  );
};
