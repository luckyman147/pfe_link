import React from 'react';
import { ArrowRight } from 'lucide-react';

interface SupervisionItemProps {
  title: string;
  type: string;
  status: 'On Track' | 'Needs Review' | 'Delayed';
  students: { name: string; avatar: string }[];
  progress: number;
  milestone: string;
}

export const SupervisionItem: React.FC<SupervisionItemProps> = ({
  title,
  type,
  status,
  students,
  progress,
  milestone,
}) => {
  const getStatusStyles = () => {
    switch (status) {
      case 'On Track': return 'bg-green/10 text-green border-green/20';
      case 'Needs Review': return 'bg-gold/10 text-gold border-gold/20';
      case 'Delayed': return 'bg-error/10 text-error border-error/20';
      default: return 'bg-surface-container-high text-on-surface-variant border-outline-variant';
    }
  };

  return (
    <div className="p-6 hover:bg-surface-container-low transition-all duration-300 group cursor-pointer border-b border-outline-variant/30 last:border-0">
      <div className="flex justify-between items-start mb-4">
        <div className="space-y-1.5">
          <h4 className="font-display font-black text-on-surface text-lg group-hover:text-primary transition-colors leading-tight">
            {title}
          </h4>
          <p className="text-xs font-bold text-on-surface-variant/60 uppercase tracking-widest">{type}</p>
        </div>
        <span className={`text-[10px] font-black uppercase tracking-[0.15em] px-3 py-1.5 rounded-full border ${getStatusStyles()}`}>
          {status}
        </span>
      </div>

      <div className="flex items-center justify-between mt-8">
        <div className="flex items-center gap-4">
          <div className="flex -space-x-3">
            {students.map((student, i) => (
              <div key={i} className="w-10 h-10 rounded-2xl border-2 border-surface-container-lowest bg-surface-container-high overflow-hidden shadow-sm transition-transform group-hover:translate-y-[-2px]" title={student.name}>
                <img src={student.avatar} alt={student.name} className="w-full h-full object-cover" />
              </div>
            ))}
          </div>
          <span className="text-sm font-bold text-on-surface-variant">
            {students.map(s => s.name.split(' ')[0]).join(' & ')}
          </span>
        </div>

        <div className="w-48">
          <div className="flex justify-between text-[10px] font-black text-on-surface-variant/40 uppercase tracking-[0.15em] mb-2.5">
            <span>{milestone}</span>
            <span className="text-on-surface">{progress}%</span>
          </div>
          <div className="w-full bg-surface-container-high rounded-full h-2.5 p-0.5 shadow-inner">
            <div 
              className="bg-primary h-full rounded-full transition-all duration-1000 ease-out shadow-sm shadow-primary/20" 
              style={{ width: `${progress}%` }} 
            />
          </div>
        </div>

        <div className="opacity-0 group-hover:opacity-100 transition-all duration-300 translate-x-[-10px] group-hover:translate-x-0">
          <div className="w-10 h-10 rounded-xl bg-primary/10 flex items-center justify-center text-primary">
            <ArrowRight className="w-5 h-5" />
          </div>
        </div>
      </div>
    </div>
  );
};
