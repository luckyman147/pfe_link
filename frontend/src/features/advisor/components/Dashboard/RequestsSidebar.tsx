import React from 'react';
import { RequestItem } from '../Requests/RequestItem';
import { Mail } from 'lucide-react';

export const RequestsSidebar: React.FC = () => {
  const requests = [
    { 
      name: 'David Kim', 
      dept: 'Computer Science', 
      time: '2h ago', 
      topic: 'Optimization of Neural Networks for Edge Devices in Autonomous Vehicles.', 
      img: 'https://i.pravatar.cc/100?img=20' 
    },
    { 
      name: 'Elena Rodriguez', 
      dept: 'Data Science', 
      time: '1d ago', 
      topic: 'Predictive Modeling of Urban Traffic Flow Using Historical Ride-Sharing Data.', 
      img: 'https://i.pravatar.cc/100?img=25' 
    }
  ];

  return (
    <div className="bg-surface-container-lowest border border-outline-variant/30 rounded-[32px] shadow-sm overflow-hidden flex flex-col h-full transition-all duration-500 hover:shadow-xl hover:shadow-tertiary/5">
      <div className="px-8 py-6 border-b border-outline-variant/20 flex justify-between items-center bg-surface-container-low/30">
        <h3 className="font-display font-black text-on-surface flex items-center gap-3 tracking-tight">
          <div className="w-10 h-10 rounded-xl bg-tertiary/10 flex items-center justify-center text-tertiary shadow-inner">
            <Mail className="w-5 h-5" />
          </div>
          Recent Requests
        </h3>
        <span className="bg-error/10 text-error font-black px-3 py-1.5 rounded-full text-[10px] uppercase tracking-widest border border-error/10 animate-pulse">
          5 Pending
        </span>
      </div>
      
      <div className="flex-1 overflow-y-auto p-6 space-y-6">
        {requests.map((req, i) => (
          <RequestItem request={{
            id: '',
            student: '',
            project: '',
            date: '',
            faculty: '',
            avatar: '',
            gpa: ''
          }} key={i} {...req} />
        ))}
      </div>
      
      <div className="p-6 border-t border-outline-variant/10 bg-surface-container-low/20 text-center">
        <button className="text-on-surface-variant/40 hover:text-primary font-black text-[11px] uppercase tracking-[0.2em] transition-all hover:tracking-[0.25em]">
          View All Requests
        </button>
      </div>
    </div>
  );
};

