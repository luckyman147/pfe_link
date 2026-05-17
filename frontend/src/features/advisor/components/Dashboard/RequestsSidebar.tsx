import React from 'react';
import { RequestItem } from '../Requests/RequestItem';
import { Mail } from 'lucide-react';
import type { SelectionRequest } from '@/features/academic/types/academic.types';

interface RequestsSidebarProps {
  items: SelectionRequest[];
}

export const RequestsSidebar: React.FC<RequestsSidebarProps> = ({ items }) => {
  return (
    <div className="bg-surface-container-lowest border border-outline-variant/30 rounded-[32px] shadow-sm overflow-hidden flex flex-col h-full transition-all duration-500 hover:shadow-xl hover:shadow-tertiary/5">
      <div className="px-8 py-6 border-b border-outline-variant/20 flex justify-between items-center bg-surface-container-low/30">
        <h3 className="font-display font-black text-on-surface flex items-center gap-3 tracking-tight">
          <div className="w-10 h-10 rounded-xl bg-tertiary/10 flex items-center justify-center text-tertiary shadow-inner">
            <Mail className="w-5 h-5" />
          </div>
          Recent Requests
        </h3>
        {items.length > 0 && (
          <span className="bg-error/10 text-error font-black px-3 py-1.5 rounded-full text-[10px] uppercase tracking-widest border border-error/10 animate-pulse">
            {items.length} Pending
          </span>
        )}
      </div>
      
      <div className="flex-1 overflow-y-auto p-6 space-y-6">
        {items.length > 0 ? (
          items.map((req, i) => (
            <RequestItem request={{
              id: req.id,
              student: req.studentId,
              project: req.projectTitle,
              date: new Date().toISOString(),
              faculty: '',
              avatar: '',
              gpa: ''
            }} key={i} name={req.studentId} dept="Student" time="Recently" topic={req.message} img="" />
          ))
        ) : (
          <div className="text-center text-on-surface-variant py-8">
            No pending requests
          </div>
        )}
      </div>
      
      <div className="p-6 border-t border-outline-variant/10 bg-surface-container-low/20 text-center">
        <button className="text-on-surface-variant/40 hover:text-primary font-black text-[11px] uppercase tracking-[0.2em] transition-all hover:tracking-[0.25em]">
          View All Requests
        </button>
      </div>
    </div>
  );
};