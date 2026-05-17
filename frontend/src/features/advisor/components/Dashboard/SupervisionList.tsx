import React from 'react';
import { TrendingUp, LayoutGrid } from 'lucide-react';
import { SupervisionItem } from './SupervisionItem';
import type { SelectionRequest } from '@/features/academic/types/academic.types';

interface SupervisionListProps {
  items: SelectionRequest[];
}

export const SupervisionList: React.FC<SupervisionListProps> = ({ items }) => {
  return (
    <div className="bg-surface-container-lowest border border-outline-variant/30 rounded-[32px] shadow-sm overflow-hidden transition-all duration-500 hover:shadow-xl hover:shadow-primary/5">
      <div className="px-8 py-6 border-b border-outline-variant/20 flex justify-between items-center bg-surface-container-low/30">
        <h3 className="font-display font-black text-on-surface flex items-center gap-3 tracking-tight">
          <div className="w-10 h-10 rounded-xl bg-primary/10 flex items-center justify-center text-primary shadow-inner">
            <TrendingUp className="w-5 h-5" />
          </div>
          Active Supervisions
        </h3>
        <button className="text-on-surface-variant hover:text-primary font-black text-[11px] uppercase tracking-[0.2em] flex items-center gap-2 transition-all group px-4 py-2 rounded-xl hover:bg-primary/5">
          <LayoutGrid className="w-4 h-4 transition-transform group-hover:rotate-90" />
          Manage All
        </button>
      </div>
      <div className="flex flex-col">
        {items.length > 0 ? (
          items.map((item, index) => (
            <SupervisionItem 
              key={index} 
              title={item.projectTitle}
              type="PFE Project"
              status="On Track"
              students={[{ name: `Student ${index + 1}`, avatar: '' }]}
              progress={50}
              milestone="In Progress"
            />
          ))
        ) : (
          <div className="p-8 text-center text-on-surface-variant">
            No active supervisions yet
          </div>
        )}
      </div>
      <div className="p-4 bg-surface-container-low/20 text-center border-t border-outline-variant/10">
        <p className="text-[10px] font-bold text-on-surface-variant/40 uppercase tracking-[0.2em]">
          {items.length > 0 ? 'Scroll to see more' : 'No active projects'}
        </p>
      </div>
    </div>
  );
};